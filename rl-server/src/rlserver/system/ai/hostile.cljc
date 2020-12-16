(ns rlserver.system.ai.hostile
  (:require
    [rllib.vector :refer [add man-dist]]
    [rllib.board :refer [small-neighborhood move]]
    [rlserver.reducer.move :refer [moveable? entity-move]]
    [rlserver.system.ai.follow :refer [step-closer move-towards-entity]]
    [rlserver.reducer.attack :refer [entity-attack]]
    [rlserver.entity.state :refer [apply-seq]]))

(defn ai-move-rand [game id]
  (let [dir (rand-nth [:up :down :left :right])]
    (if (moveable? game dir id)
      (entity-move game id dir)
      game)))

(defn attack-towards-entity [game id targetid]
  (let [origin (get-in game [:pos id])
        target (get-in game [:pos targetid])
        dir (step-closer origin target)]
    (entity-attack game id dir)))

(defn ai-move-hostile [game id]
  (let [originpos (get-in game [:pos id])
        closest (fn [[pc pos]] (man-dist originpos pos))
        [targetid targetpos] (apply min-key closest (select-keys (:pos game) (:pc game)))
        dist (man-dist originpos targetpos)]
    (cond
      (<= dist 1) (attack-towards-entity game id targetid)
      (< dist 12) (move-towards-entity game id targetid)
      true (ai-move-rand game id))))