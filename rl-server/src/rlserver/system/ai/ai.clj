(ns rlserver.system.ai.ai
  (:require
    [rllib.vector :refer [add man-dist]]
    [rllib.board :refer [small-neighborhood move]]
    [rlserver.system.ai.follow :refer [ai-move-follow]]
    [rlserver.system.ai.hostile :refer [ai-move-hostile]]
    [rlserver.reducer.move :refer [moveable? entity-move]]
    [rlserver.reducer.attack :refer [entity-attack]]
    [rlserver.entity.state :refer [apply-seq]]))

(defn ai-move-rand [game id]
  (let [dir (rand-nth [:up :down :left :right])]
    (if (moveable? game dir id)
      (entity-move game id dir)
      game)))

(defn ai-decide [game id]
  (let [hostile? (contains? (set (:hostile game)) id)
        follow? (contains? (:follow game) id)]
    (cond
      hostile? (ai-move-hostile game id)
      follow? (ai-move-follow game id)
      true (ai-move-rand game id))))

(defn update-ai [game]
  (apply-seq game (:ai game) ai-decide))
