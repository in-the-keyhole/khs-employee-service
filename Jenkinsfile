node() {
  stage('Check Quality') {
    git credentialsId: 'ci-cd-github-secret', branch: 'openshift-cicd', url: 'git@github.com:in-the-keyhole/khs-employee-service'
    sh "mvn help:effective-pom"
    sh "mvn verify sonar:sonar"
  }  
}