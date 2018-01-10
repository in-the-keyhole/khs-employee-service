node('maven') {
  stage('Run Unit Tests') {
    git credentialsId: 'ci-cd-github-secret', branch: 'openshift-cicd', url: 'git@github.com:in-the-keyhole/khs-employee-service'
    sh "mvn test"
  }
  stage('Check Quality') {
    sh "mvn verify sonar:sonar"
  }
  stage('Build Image') {
    openshiftBuild(namespace: 'dev-reference', buildConfig: 'employee-service', showBuildLogs: 'true')
  }
  stage('Deploy') {
    openshiftDeploy(namespace: 'dev-reference', deploymentConfig: 'employee-service')  
  }  
}