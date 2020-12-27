(ns rlserver.system.effect
  (:require [rlserver.entity.entity :refer [destroy-entity]]
            [rlserver.entity.state :refer [apply-seq]]))

(defn update-timer [state [k timer]]
  (if (zero? timer) (destroy-entity state k)
                    (update-in state [:timer k] dec)))

(defn clean-timers [game]
  (apply-seq game (:timer game) update-timer))