//Basis Pipeline Schema anlegen

pipeline {
  agent any
  stages {
    stage('Building Feature') {
      when {
              branch 'feature/*'
              beforeAgent true
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