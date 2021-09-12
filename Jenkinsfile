pipeline {
  agent any

  tools {
    maven 'maven'
  }

  stages {
    stage('Build & Test') {
      sh 'mvn -Dmaven.test.failure.ignore=true install'
    }
  }
}
