(ns rlclient.network.connect
  (:require
    [cljs.reader :as reader]
    [rlclient.network.session :as r]))


(def remote-state (atom {}))

(def socket (atom nil))

(defn send! [msg]
  (.send @socket msg))

(defn connect-socket! []
  (let [protocol (if r/secure? "wss:" "ws:")
        channel (str protocol "//" r/server "/game/" (r/game-id) "/player/" (r/player-id) "/ws")]
    (println "connecting to socket" channel)
    (reset! socket (js/WebSocket. channel))

    (set! (.-onopen @socket)
          (fn [] (send! (str "connect"))))

    (set! (.-onerror @socket)
          (fn [] (js/alert "error")))

    (set! (.-onmessage @socket)
          (fn [event]
            (let [data (reader/read-string (.-data event))]
              (cond (map? data) (swap! remote-state merge data)
                    (nil? data) (send! "reset")
                    true (println "string:" data)))))))

(defn reconnect! []
  (send! "disconnect")
  (connect-socket!))