(defproject rlserver "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]

                 [ruiyun/tools.timer "1.0.1"]
                 [compojure "1.6.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [http-kit "2.3.0"]
                 [environ "1.1.0"]

                 [org.clojure/data.codec "0.1.1"]
                 [org.clojure/data.json "1.0.0"]]

  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "server-standalone.jar"
  :main rlserver.core
  :aot [rlserver.core])
