(ns rlserver.system.ai
  (:require
    [rllib.vector :refer [add man-dist]]
    [rllib.math :refer [small-neighborhood]]
    [rlserver.reducer.move :refer [moveable? entity-move move]]
    [rlserver.reducer.attack :refer [entity-attack]]
    [rlserver.entity.state :refer [apply-seq]]))

(defn step-closer [origin target]
  (first (apply min-key (fn [[k p]] (man-dist p target))
                (shuffle (seq (small-neighborhood origin))))))

(defn attack-towards-entity [game id targetid]
  (let [origin (get-in game [:pos id])
        target (get-in game [:pos targetid])
        dir (step-closer origin target)]
    (entity-attack game id dir)))

(defn move-towards-entity [game id targetid]
  (let [origin (get-in game [:pos id])
        target (get-in game [:pos targetid])
        dir (step-closer origin target)]
    (if (moveable? game dir id)
      (entity-move game id dir)
      game)))

(defn move-ai-rand [game id]
  (let [dir (rand-nth [:up :down :left :right])]
    (if (moveable? game dir id)
      (entity-move game id dir)
      game)))

(defn ai-decide [game id]
  (let [origin (get-in game [:pos id])
        target (get-in game [:pos 0])
        dist (man-dist origin target)]
    (cond
      (<= dist 1) (attack-towards-entity game id 0)
      (< dist 10) (move-towards-entity game id 0)
      true (move-ai-rand game id))))

(defn update-ai [game]
  (apply-seq game (:ai game) ai-decide))
