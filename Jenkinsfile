node('maven') {

  stage('Run Unit Tests') {
     git credentialsId: 'ci-cd-github-secret', branch: 'openshift-cicd', url: 'git@github.com:in-the-keyhole/khs-employee-service' 
     withMaven(
        // Maven installation declared in the Jenkins "Global Tool Configuration"
        maven: 'M3',
        // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
        // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
        mavenSettingsConfig: 'global-maven-settings',
        mavenLocalRepo: '.repository') {
 
      // Run the maven build
      sh "mvn test"
 
    } 
  }
  
  stage('Check Quality') {
  
   git credentialsId: 'ci-cd-github-secret', branch: 'openshift-cicd', url: 'git@github.com:in-the-keyhole/khs-employee-service'
   withMaven(
        // Maven installation declared in the Jenkins "Global Tool Configuration"
        maven: 'M3',
        // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
        // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
        mavenSettingsConfig: 'global-maven-settings',
        mavenLocalRepo: '.repository') {
 
      // Run the maven build
      sh "mvn verify sonar:sonar"
 
    }
  }  
  
  stage('Build Image') {
    openshiftBuild(namespace: 'dev-reference', buildConfig: 'employee-service', showBuildLogs: 'true')
  }
  
  stage('Deploy') {
    openshiftDeploy(namespace: 'dev-reference', deploymentConfig: 'employee-service')  
  }  
}