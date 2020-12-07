(ns rlclient.network.session
  (:require [clojure.string :as str]))

(def server (str (. (. js/document -location) -hostname) ":5000"))
(def host (. (. js/document -location) -host))
(def origin (. (. js/document -location) -origin))
(def path (. (. js/document -location) -pathname))

(def session (atom {:pid 0
                    :gid 0}))

(defn cycle-pid! []
  (swap! session update :pid (fn [pid] (mod (inc pid) 2))))

(defn player-id [] (get @session :pid))
(defn game-id [] (get @session :gid))

(def secure? (= "https:" (. (. js/document -location) -protocol)))
