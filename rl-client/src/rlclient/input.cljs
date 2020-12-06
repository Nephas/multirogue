(ns rlclient.input
  (:require [quil.core :as q]
            [rlclient.network.connect :refer [send]]
            [rlclient.network.session :refer [swap-player!]]))

(def coded-keys {32 :space
                 8  :back
                 9  :tab
                 10 :enter})

(defn commit! [action]
  (println (str "action " action))
  (send (str "action " action)))

(defn reset! []
  (send "reset"))

(def actions {:w     [:move :up]
              :a     [:move :left]
              :s     [:move :down]
              :d     [:move :right]

              :1     [:item :1]
              :2     [:item :2]
              :3     [:item :3]

              :t     [:pass nil]
              :space [:attack nil]})

(defn handle-key [state event]
  (let [coded-key (get coded-keys (q/key-code))
        key (if (some? coded-key) coded-key (:key event))]
    (do (println "keypress: " (q/key-code) " - " (:key event))
        (cond (= key :r) (reset!)
              (= key :Dead) (swap-player!)
              (contains? actions key) (commit! (get actions key)))
        state)))