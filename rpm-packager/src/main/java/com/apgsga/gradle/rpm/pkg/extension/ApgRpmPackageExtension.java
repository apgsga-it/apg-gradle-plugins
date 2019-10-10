package com.apgsga.gradle.rpm.pkg.extension;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class ApgRpmPackageExtension {
	
	private static final String SERVICE_PROPERTIES_DIR_DEFAULT = "resources";
	private static final String APG_OPSDEFAULT = "apg_ops";
	private static final String RELEASENR_DEFAULT = "1";
	private static final String VERSION_DEFAULT = "0.1";
	private static final String JDK_DIST_REPO_NAME_DEFAULT = "apgPlatformDependencies";
	private static final String REPO_BASE_URL_DEFAULT = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga/";
	private static final String JAVADIST_DEFAULT = "jdk-8u191-linux-x64.tar.gz";
	private static final String JAVADIR_DEFAULT = "jdk1.8.0_191";
	private static final boolean WEBEMMEDED_DEFAULT = false;
	private static final String SPRING_PROFILES_DEFAULT = "stomprelay";
	private static final String SERVER_CONTEXT_PATH_DEFAULT = "/";
	private static final String DS_DAOLOCATIONS_DEFAULTS = "com/affichage/ibds/ds/**";
	private static final String IT21_DAO_LOCATIONS_DEFAULTS = "com/apgsga/common/** com/affichage/it21/** com/apgsga/it21/** com/affichage/mybatis/** com/affichage/persistence/** com/affichage/testsupport/**";
	private static final String DATAACCESS_STRATEGIE_DEFAULT = "TRX_PER_OP_WITH_STANDING_USRCRED_CONNECTION";
	private static final String APP_CONFIG_FILTER_DEFAULT = "webui:general";
	private static final String RESOURCE_FILTER_DEFAULT = "serviceport:it21db-webui:ibdsdb-jadas:ecmservice";
	private static final String SEC_DEP_DEFAULT = "com.apgsga.it21.ui.mdt:jadas-framework-starter:9.1.0.DIGIFLEX-SNAPSHOT";
	private static final String FIRST_DEP_DEFAULT = "com.apgsga.it21.ui.mdt:jadas-app-starter:9.1.0.DIGIFLEX-SNAPSHOT";
	private static final String TARGET_DEFAULT = "CHEI212";
	private static final String MAIN_PRG_DEFAULT = "com.apgsga.it21.ui.webapp.Webapp";
	private static final String SERVICE_NAME_DEFAULT = "jadas";
	// TODO (che, 25.9) : probably not the perfect places 
	private static final List<String> SUPPORTED_SERVICE_NAMES_DEFAULT = Lists.newArrayList("jadas", "digiflex","vkjadas", "interjadas", "interweb"); 
	
	
	private String serviceName =  SERVICE_NAME_DEFAULT; 
	private String mainProgramName = MAIN_PRG_DEFAULT; 
	private String installTarget = TARGET_DEFAULT; 
	private String[] dependencies = new String[] {FIRST_DEP_DEFAULT,SEC_DEP_DEFAULT};
	private String resourceFilters = RESOURCE_FILTER_DEFAULT; 
	private String appConfigFilters = APP_CONFIG_FILTER_DEFAULT; 
	private String dataAccessStrategie =  DATAACCESS_STRATEGIE_DEFAULT; 
	private String it21DbDaoLocations = IT21_DAO_LOCATIONS_DEFAULTS;
	private String ibdsDbDaoLocations = DS_DAOLOCATIONS_DEFAULTS; 
	private String serverContextPath = SERVER_CONTEXT_PATH_DEFAULT;
	private String springProfiles =  SPRING_PROFILES_DEFAULT; 
	private Boolean webuiEmbedded = WEBEMMEDED_DEFAULT;
	private String javaDir = JAVADIR_DEFAULT; 
	private String javaDist = JAVADIST_DEFAULT; 
	// TODO (che, 25.9) : retrieve baseUrl from common-repo Plugin 
	private String distRepoUrl = REPO_BASE_URL_DEFAULT + JDK_DIST_REPO_NAME_DEFAULT; 
	private List<String> supportedServices = SUPPORTED_SERVICE_NAMES_DEFAULT; 
	private String version = VERSION_DEFAULT; 
	private String releaseNr = RELEASENR_DEFAULT; 
	private String opsUserGroup = APG_OPSDEFAULT; 
	private String servicePropertiesDir = SERVICE_PROPERTIES_DIR_DEFAULT; 
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getMainProgramName() {
		return mainProgramName;
	}
	public void setMainProgramName(String mainProgramName) {
		this.mainProgramName = mainProgramName;
	}
	public String[] getDependencies() {
		return dependencies;
	}
	public void setDependencies(String[] dependencies) {
		this.dependencies = dependencies;
	}
	public String getResourceFilters() {
		return resourceFilters;
	}
	public void setResourceFilters(String resourceFilters) {
		this.resourceFilters = resourceFilters;
	}
	public String getAppConfigFilters() {
		return appConfigFilters;
	}
	public void setAppConfigFilters(String appConfigFilters) {
		this.appConfigFilters = appConfigFilters;
	}
	public String getDataAccessStrategie() {
		return dataAccessStrategie;
	}
	public void setDataAccessStrategie(String dataAccessStrategie) {
		this.dataAccessStrategie = dataAccessStrategie;
	}
	public String getIt21DbDaoLocations() {
		return it21DbDaoLocations;
	}
	public void setIt21DbDaoLocations(String it21DbDaoLocations) {
		this.it21DbDaoLocations = it21DbDaoLocations;
	}
	public String getIbdsDbDaoLocations() {
		return ibdsDbDaoLocations;
	}
	public void setIbdsDbDaoLocations(String ibdsDbDaoLocations) {
		this.ibdsDbDaoLocations = ibdsDbDaoLocations;
	}
	public String getServerContextPath() {
		return serverContextPath;
	}
	public void setServerContextPath(String serverContextPath) {
		this.serverContextPath = serverContextPath;
	}
	public String getSpringProfiles() {
		return springProfiles;
	}
	public void setSpringProfiles(String springProfiles) {
		this.springProfiles = springProfiles;
	}
	public Boolean getWebuiEmbedded() {
		return webuiEmbedded;
	}
	public void setWebuiEmbedded(Boolean webEmbedded) {
		this.webuiEmbedded = webEmbedded;
	}
	public String getInstallTarget() {
		return installTarget;
	}
	public void setInstallTarget(String installTarget) {
		this.installTarget = installTarget;
	}
	
	public String getJavaDir() {
		return javaDir;
	}
	public void setJavaDir(String javaDir) {
		this.javaDir = javaDir;
	}
	public String getJavaDist() {
		return javaDist;
	}
	public void setJavaDist(String javaDist) {
		this.javaDist = javaDist;
	}
	public String getDistRepoUrl() {
		return distRepoUrl;
	}
	public void setDistRepoUrl(String distRepoUrl) {
		this.distRepoUrl = distRepoUrl;
	}
	
	public List<String> getSupportedServices() {
		return supportedServices;
	}
	public void setSupportedServices(List<String> supportedServices) {
		this.supportedServices = supportedServices;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getReleaseNr() {
		return releaseNr;
	}
	public void setReleaseNr(String releaseNr) {
		this.releaseNr = releaseNr;
	}
	
	
	public String getOpsUserGroup() {
		return opsUserGroup;
	}
	public void setOpsUserGroup(String opsUserGroup) {
		this.opsUserGroup = opsUserGroup;
	}
	
	public String getServicePropertiesDir() {
		return servicePropertiesDir;
	}
	public void setServicePropertiesDir(String servicePropertiesDir) {
		this.servicePropertiesDir = servicePropertiesDir;
	}
	// Logging 
	public void log() {
		toString();
	}
	
	// Conventions 
	
	public String getTargetServiceName() {
		return "apg-" + getServiceName() + "-" + getInstallTarget(); 
	}
	
	public String getTargetServiceExecDir() {
		return "/opt/" + getTargetServiceName();
	}
	
	public String getTargetServiceDataDir() {
		return "/var/opt/" +  getTargetServiceName();
	}
	
	public String getTargetServiceConfigDir() {
		return "/etc/opt/" +  getTargetServiceName();
	}
	
	public String getPortNr() {
		return PortnrConvention.calculate(getSupportedServices(), getInstallTarget(), getServiceName()); 
	}
	
	public String getEcmTargetSystemInd() {
		return  getInstallTarget().charAt(2) == 't' ? "t" : "p";
	}
	
	public String getIbdsTargetSystemInd() {
		return Character.toString(getInstallTarget().charAt(2)).toLowerCase(); 
	}
	
	// TODO (che, 9.10) Avoid redundancy with buildRpm Task of netflix ospackage
	public String getArchiveName() {
		return getTargetServiceName() + "-" + getVersion() + "-" + getReleaseNr()+ ".noarch.rpm"; 
	}
	@Override
	public String toString() {
		return "ApgRpmPackageExtension [serviceName=" + serviceName + ", mainProgramName=" + mainProgramName
				+ ", installTarget=" + installTarget + ", dependencies=" + Arrays.toString(dependencies)
				+ ", resourceFilters=" + resourceFilters + ", appConfigFilters=" + appConfigFilters
				+ ", dataAccessStrategie=" + dataAccessStrategie + ", it21DbDaoLocations=" + it21DbDaoLocations
				+ ", ibdsDbDaoLocations=" + ibdsDbDaoLocations + ", serverContextPath=" + serverContextPath
				+ ", springProfiles=" + springProfiles + ", webuiEmbedded=" + webuiEmbedded + ", javaDir=" + javaDir
				+ ", javaDist=" + javaDist + ", distRepoUrl=" + distRepoUrl + ", supportedServices=" + supportedServices
				+ ", version=" + version + ", releaseNr=" + releaseNr + ", opsUserGroup=" + opsUserGroup
				+ ", servicePropertiesDir=" + servicePropertiesDir + "]";
	}
	
	
	

}
