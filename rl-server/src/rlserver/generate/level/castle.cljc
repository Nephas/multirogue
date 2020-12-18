(ns rlserver.generate.level.castle
  (:require [rlserver.generate.object :refer [generate-stair-up generate-stair-down generate-corpse generate-potion generate-runestone]]
            [rllib.board :refer [midpoint]]
            [rlserver.generate.level.room :refer [rand-field]]
            [rlserver.entity.state :refer [apply-seq]]
            [rlserver.generate.npc :refer [generate-raven]]
            [rllib.rand :refer [rand-coll rand-bool]]))

(defn generate-ravens [state tier-defs]
  (let [rooms (last tier-defs)]
    (apply-seq state rooms (fn [s r] (if (rand-bool) (generate-raven s (rand-field r)) s)))))

(defn generate-castle-level [state tier-defs corridor-defs]
  (-> state
      (generate-runestone (rand-field (rand-coll (last tier-defs))))
      (generate-ravens tier-defs)))