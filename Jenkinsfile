node('maven') {
  stage('Build') {
    git credentialsId: 'github-credentials', branch: 'openshift', url: 'https://github.com/in-the-keyhole/khs-employee-service.git'
    sh "mvn package -DskipTests=true"
  }
  stage('Test') {
    sh "mvn test"
  }
  stage('Build Image') {
    openshiftBuild(buildConfig: 'employee-service', showBuildLogs: 'true')
  }
  stage('Deploy') {
    openshiftDeploy(deploymentConfig: 'employee-service')  
  }  
}