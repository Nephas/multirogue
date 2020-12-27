(ns rlserver.loop
  (:require [ruiyun.tools.timer :as t]
            [rllib.constant :refer [TICSIZE]]
            [rlserver.system.update :as u]
            [rlserver.generate.level.level :refer [generate-level with-new-pcs]]
            [rlserver.entity.state :refer [INITSTATE serialize-diff game-store seq-store]]))

(def timers (atom {}))

(defn start-loop! [id broadcaster]
  (if (nil? (get @timers id))
    (let [timer (t/timer (str id))]
      (swap! timers assoc id timer)
      ;(t/run-task! #(u/update id broadcaster) :period TICSIZE :by timer)

      )))

(defn stop-loop! [id]
  (t/cancel! (get @timers id))
  (swap! timers assoc id nil))


(defn destroy-game [id]
  (do (swap! game-store dissoc id)
      (swap! game-store dissoc (dec id))))

(defn preload-game [id]
  (do (println "\t * new game")
      (destroy-game id)
      (swap! game-store assoc id (assoc INITSTATE :load 0))))

(defn initialize-game [id]
  (do (println "\t * generating new level")
      (swap! game-store update id #(-> %
                                       (generate-level 1 0)
                                       (with-new-pcs)))))
