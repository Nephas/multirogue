(ns rlclient.network.session
  (:require [clojure.string :as str]))

(def server (str (. (. js/document -location) -hostname) ":5000"))
(def host (. (. js/document -location) -host))
(def origin (. (. js/document -location) -origin))
(defn path [] (. (. js/document -location) -pathname))

(def session (atom {:pid 0
                    :gid 0}))

(defn cycle-pid! []
  (swap! session update :pid (fn [pid] (mod (inc pid) 2))))

(defn player-id [] (-> (path)
                       (str/split #"player/")
                       (second)
                       (str/split #"/")
                       (first)
                       (js/parseInt)))

(defn other-player [] (mod (inc (player-id)) 2))

(defn game-id [] (-> (path)
                     (str/split #"game/")
                     (second)
                     (str/split #"/")
                     (first)
                     (js/parseInt)
                     ))

(def secure? (= "https:" (. (. js/document -location) -protocol)))
