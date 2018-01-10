node('maven') {
  
  stage('Check Quality') {
    sh "mvn -v verify sonar:sonar"
  }  
}