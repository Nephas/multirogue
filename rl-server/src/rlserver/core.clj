(ns rlserver.core
  (:gen-class)
  (:require [compojure.core :refer [context defroutes GET ANY]]
            [compojure.route :as route]

            [org.httpkit.server :as http]
            [environ.core :refer [env]]
            [rlserver.entity.state :refer [serialize-json]]
            [rlserver.socket :refer [handle]]
            [clojure.java.io :as io]))



(defn ws-handler [gid pid]
  (fn [req] (http/with-channel req channel
                               (http/on-close channel (fn [_] (handle "disconnect" channel gid pid)))
                               (http/on-receive channel (fn [msg] (handle msg channel gid pid))))))

(defroutes routes
           (route/resources "/")
           (GET "/" [] (slurp (io/resource "index.html")))
           (context "/game/:gid" [gid]
             (GET "/state" [] (serialize-json gid))
             (context "/player/:pid" [pid]
               (GET "/" [] (slurp (io/resource "index.html")))
               (GET "/ws" [] (ws-handler (Integer/parseInt gid) (Integer/parseInt pid))))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (println (str "backend - http://localhost:" port))
    (http/run-server routes {:port port})))
