import com.cloudbees.groovy.cps.NonCPS

modules = ['../testapp-module','../testapp-service']

node('master') {
    stage('Maven build') {
        echo "building in:"
        maven_build(modules)
    }
}
@NonCPS // has to be NonCPS or the build breaks on the call to .each
def maven_build(list) {
    list.each { item ->
        sh "mvn clean install"
    }
}
