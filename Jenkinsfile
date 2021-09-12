pipeline {
  agent any

  tools {
    maven 'maven 3.8'
  }

  stages {
    stage('Build & Test') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true install'
      }
    }
  }
}
