node('maven') {

  stage('Check Quality') {
    sh "mvn verify sonar:sonar"
  }  
}