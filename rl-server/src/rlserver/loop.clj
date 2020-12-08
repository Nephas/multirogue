(ns rlserver.loop
  (:require [ruiyun.tools.timer :as t]
            [rlserver.system.update :as u]
            [rlserver.generate.level :as gl]
            [rlserver.entity.state :refer [INITSTATE serialize-game game-store seq-store]]))

(def timers (atom {}))

(defn start-loop! [id broadcaster]
  (if (nil? (get @timers id))
    (let [timer (t/timer id)]
      (swap! timers assoc id timer)
      (t/run-task! #(u/update id broadcaster) :period 750 :by timer))))

(defn stop-loop! [id]
  (t/cancel! (get @timers id))
  (swap! timers assoc id nil))

(defn initialize-game [id broadcaster]
  (let [state (gl/generate-level INITSTATE [48 48])]
    (swap! game-store assoc id state)
    (start-loop! id broadcaster)))