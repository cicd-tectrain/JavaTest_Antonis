//Basis Pipeline Schema anlegen

pipeline {
  agent any
  stages {
    stage('Building Feature') {
      when {
              branch 'feature/*'
              beforeAgent true
            }

      //Agent overwrite and run in a docker container
      agent {
        docker {
            image '7.5.1-jdk17-focal'
        }
      }


      steps {
        echo 'Building feature'
      }
    }

    stage('Testing Feature') {
      when {
              branch 'feature/*'
              beforeAgent true
            }

      steps {
        echo 'Testing feature'
      }
    }

    stage('Integrating Feature') {
      when {
        branch 'feature/*'
        beforeAgent true
      }

      steps {
        echo 'Integrating feature'
      }
    }

  }
}