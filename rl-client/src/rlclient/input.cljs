(ns rlclient.input
  (:require [quil.core :as q]
            [rlclient.network.connect :refer [send!]]
            [rlclient.network.session :refer [cycle-pid!]]
            [rlclient.network.connect :as c]))

(def coded-keys {32 :space
                 8  :back
                 9  :tab
                 10 :enter})

(defn send-commit! [action]
  (println (str "action " action))
  (send! (str "action " action)))

(defn send-reset! []
  (send! "reset"))

(defn swap-player! []
  (cycle-pid!)
  (c/send! "disconnect")
  (c/connect-socket!))

(def actions {:w          [:move :up]
              :a          [:move :left]
              :s          [:move :down]
              :d          [:move :right]

              :ArrowUp    [:attack :up]
              :ArrowLeft  [:attack :left]
              :ArrowDown  [:attack :down]
              :ArrowRight [:attack :right]

              :1          [:item :1]
              :2          [:item :2]
              :3          [:item :3]

              :t          [:pass nil]
              :space      [:attack nil]})

(def current-key (atom nil))

(defn handle-key [key]
  (cond (= key :r) (send-reset!)
        (= key :Dead) (swap-player!)
        (contains? actions key) (send-commit! (get actions key))))

(defn handle-event [state event]
  (let [key (q/key-as-keyword)]
    (print key)
    (do (reset! current-key {:key key :time (q/frame-count)})
        (handle-key key)
        state)))

(defn refire-key []
  (when (and (q/key-pressed?) (zero? (mod (- (q/frame-count) (:time @current-key)) 2)))
    (handle-key (:key @current-key))))
