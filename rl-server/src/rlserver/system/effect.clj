(ns rlserver.system.effect
  (:require [rlserver.entity.entity :refer [destroy-entity]]
            [rlserver.entity.state :refer [apply-seq]]))

(defn clean-effects [game]
  (let [effects (keys (:effect game))]
    (apply-seq game effects destroy-entity)))