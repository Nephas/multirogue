(defproject rlclient "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.3.610"]
                 [org.clojure/clojurescript "1.10.520"]
                 [quil "3.1.0"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.19"]]
  :hooks [leiningen.cljsbuild]

  :clean-targets ^{:protect false} ["resources/public/js"]
  :cljsbuild
  {:builds [; development build with figwheel hot swap
            {:id           "development"
             :source-paths ["src"]
             :figwheel     {:load-warninged-code false}
             :compiler     {:main       "rlclient.core"
                            :output-to  "resources/public/js/main.js"
                            :output-dir "resources/public/js/development"
                            :asset-path "js/development"}}
            ; minified and bundled build for deployment
            {:id           "optimized"
             :source-paths ["src"]
             :compiler
                           {:main          "rlclient.core"
                            :output-to     "resources/public/js/main.js"
                            :output-dir    "resources/public/js/optimized"
                            :asset-path    "js/optimized"
                            :optimizations :advanced}}]})
