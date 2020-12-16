(ns rlserver.system.ai.follow
  (:require [rlserver.reducer.move :refer [moveable? entity-move]]
            [rllib.vector :refer [add man-dist]]
            [rllib.board :refer [small-neighborhood move]]))

(defn step-closer [origin target]
  (first (apply min-key (fn [[k p]] (man-dist p target))
                (shuffle (seq (small-neighborhood origin))))))

(defn move-towards-entity [game id targetid]
  (let [origin (get-in game [:pos id])
        target (get-in game [:pos targetid])
        dir (step-closer origin target)]
    (if (moveable? game dir id)
      (entity-move game id dir)
      game)))

(defn ai-move-follow [game id]
  (let [targetid (get-in game [:follow id])
        originpos (get-in game [:pos id])
        targetpos (get-in game [:pos targetid])
        dist (man-dist originpos targetpos)]
    (if (< 2 dist 12) (move-towards-entity game id targetid)
                      game)))