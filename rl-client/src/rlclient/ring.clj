(ns rlclient.ring
  (:require [compojure.core :refer [context defroutes GET ANY]]
            [compojure.route :as route]))

(defroutes handler
           (context "/game/:gid/player/:pid" [gid pid]
             (route/resources "/")
             (route/files "/img")
             (route/files "/cljs-out")))
