(ns rlclient.input.input
  (:require [quil.core :as q]
            [rllib.constant :refer [ACTIONRATE FRAMERATE]]
            [rlclient.network.connect :refer [send!]]
            [rlclient.input.action :refer [context-action actions player-alive?]]
            [rlclient.network.session :refer [cycle-pid!]]))

(defn send-commit! [action]
  (println (str "action " action))
  (send! (str "action " action)))

(defn send-reset! []
  (send! "reset"))

(def current-key (atom nil))

(defn handle-key [key]
  (cond (= key :r) (send-reset!)
        (contains? actions key) (let [action (context-action key)]
                                  (when (some? action) (send-commit! action)))))

(defn handle-event [state event]
  (let [key (q/key-as-keyword)]
    (do (reset! current-key {:key key :time (q/frame-count)})
        (handle-key key)
        state)))

(defn refire-key []
  (when (and (q/key-pressed?) (zero? (mod (- (q/frame-count) (:time @current-key)) (int (/ FRAMERATE ACTIONRATE)))))
    (handle-key (:key @current-key))))
