(ns rlserver.system.ai
  (:require
    [rllib.vector :refer [add man-dist]]
    [rllib.board :refer [small-neighborhood move]]
    [rlserver.reducer.move :refer [moveable? entity-move]]
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
  (let [hostile? (contains? (set (:hostile game)) id)
        originpos (get-in game [:pos id])
        [targetid targetpos] (apply min-key (fn [[pc pos]]
                                              (man-dist originpos pos)) (select-keys (:pos game) (:pc game)))
        dist (man-dist originpos targetpos)]
    (cond
      (and hostile? (<= dist 1)) (attack-towards-entity game id targetid)
      (and hostile? (< dist 10)) (move-towards-entity game id targetid)
      true (move-ai-rand game id))))

(defn update-ai [game]
  (apply-seq game (:ai game) ai-decide))
