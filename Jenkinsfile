node('maven') {
  stage('Run Unit Tests') {
    git credentialsId: 'ci-cd-github-secret', branch: 'openshift-cicd', url: 'git@github.com:in-the-keyhole/khs-employee-service'
    sh "mvn test"
  }
  stage('Build Image') {
    openshiftBuild(buildConfig: 'employee-service', showBuildLogs: 'true')
  }
  stage('Deploy') {
    openshiftDeploy(deploymentConfig: 'employee-service')  
  }  
}