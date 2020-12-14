(ns rlserver.socket
  (:require [clojure.string :as str]
            [rlserver.entity.state :as s]
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
            (let [this? #(= % channel)]
              (do (swap! channels update gid #(remove this? %))
                  (println "\t * open channels:" (count (get @channels gid)))
                  (when (empty? (get @channels gid))
                    (println "\t * closing game")
                    (l/destroy-game gid))))

            (str/includes? msg "reset")
            (do (println "\t * reset level")
                (l/destroy-game gid)
                (l/initialize-game gid broadcast))

            (str/includes? msg "connect")
            (do (swap! channels update gid conj channel)
                (println "\t * open channels:" (count (get @channels gid)))
                (broadcast gid (str "connect player " pid))
                (broadcast gid (s/serialize-full gid))
                (when (nil? (get @s/game-store gid))
                  (println "\t * start new game")
                  (l/initialize-game gid broadcast)))

            (str/includes? msg "action")
            (let [action (read-string (last (str/split msg #"action")))]
              (do (swap! s/game-store update gid r/reduce action pid)
                  (println "\t * state tic:" (get-in @s/game-store [gid :tic]))
                  (broadcast gid (s/serialize-diff gid)))))))