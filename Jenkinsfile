pipeline {
  agent any

  tools {
    maven 'maven'
  }

  stages {
    stage('Build & Test') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true install'
      }
    }
  }
}
