(ns rlserver.socket
  (:require [clojure.string :as str]
            [rlserver.state :as s]
            [rlserver.loop :as l]
            [rlserver.reducer.reduce :as r]
            [org.httpkit.server :as http]))

(def channels (atom {}))

(defn broadcast [gid msg]
  (doseq [other (get @channels gid)]
    (http/send! other msg)))

(defn log-header [gid pid]
  (str "[gid: " gid " - pid: " pid "]"))

(defn handle [msg channel gid pid]
  (do (println (log-header gid pid) msg)
      (cond (str/includes? msg "disconnect")
            (l/stop-loop! gid)

            (str/includes? msg "connect")
            (do (swap! channels update gid conj channel)
                (l/initialize-game gid broadcast)
                (broadcast gid (str "connect player " pid)))

            (str/includes? msg "action")
            (let [action (read-string (last (str/split msg #"action")))]
              (do (swap! s/game-store update gid r/reduce action pid)
                  (println "\t * state tic:" (get-in @s/game-store [gid :tic]))
                  (broadcast gid (s/serialize-game gid)))))))