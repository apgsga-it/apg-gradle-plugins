package com.apgsga.common.repo.extensions;

import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuilder;
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.apache.maven.settings.building.SettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingResult;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecUtil;
import org.sonatype.plexus.components.sec.dispatcher.model.SettingsSecurity;

import java.io.File;

public class LocalMavenSettingsLoader {
    public static final File GLOBAL_SETTINGS_FILE = new File(System.getenv("M2_HOME"), "conf/settings.xml");
    public static final String SETTINGS_SECURITY_FILE_LOCATION = System.getProperty("user.home") + "/.m2/settings-security.xml";

    private final String userSettingFile;

    public LocalMavenSettingsLoader(String userSettingFile) {

        this.userSettingFile = userSettingFile;
    }

    /**
     * Loads and merges Maven settings from global and local user configuration files. Returned
     * {@link org.apache.maven.settings.Settings} object includes decrypted credentials.
     *
     * @return Effective settings
     * @throws SettingsBuildingException If the effective settings cannot be built
     */
    public Settings loadSettings() throws SettingsBuildingException {
        SettingsBuildingRequest settingsBuildingRequest = new DefaultSettingsBuildingRequest();
        settingsBuildingRequest.setUserSettingsFile(new File(userSettingFile));
        settingsBuildingRequest.setGlobalSettingsFile(GLOBAL_SETTINGS_FILE);
        settingsBuildingRequest.setSystemProperties(System.getProperties());

        DefaultSettingsBuilderFactory factory = new DefaultSettingsBuilderFactory();
        DefaultSettingsBuilder settingsBuilder = factory.newInstance();
        SettingsBuildingResult settingsBuildingResult = settingsBuilder.build(settingsBuildingRequest);
        Settings settings = settingsBuildingResult.getEffectiveSettings();
        decryptCredentials(settings);

        return settings;
    }

    private void decryptCredentials(Settings settings) {
        try {
            String masterPassword = null;
            DefaultPlexusCipher cipher = new DefaultPlexusCipher();
            File settingsSecurityFile = new File(SETTINGS_SECURITY_FILE_LOCATION);
            boolean hasSettingsSecurity = false;

            if (settingsSecurityFile.exists() && !settingsSecurityFile.isDirectory()) {
                SettingsSecurity settingsSecurity = SecUtil.read(SETTINGS_SECURITY_FILE_LOCATION, true);
                masterPassword = cipher.decryptDecorated(settingsSecurity.getMaster(), DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION);
                hasSettingsSecurity = true;
            }

            for (Server server : settings.getServers()) {
                if (cipher.isEncryptedString(server.getPassword())) {
                    if (hasSettingsSecurity) {
                        server.setPassword(cipher.decryptDecorated(server.getPassword(), masterPassword));
                    } else {
                        throw new RuntimeException("Maven settings contains encrypted credentials yet no settings-security.xml exists.");
                    }
                }

                if (cipher.isEncryptedString(server.getPassphrase())) {
                    if (hasSettingsSecurity) {
                        server.setPassphrase(cipher.decryptDecorated(server.getPassphrase(), masterPassword));
                    } else {
                        throw new RuntimeException("Maven settings contains encrypted credentials yet no settings-security.xml exists.");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to decrypt local Maven settings credentials.", e);
        }
    }
}