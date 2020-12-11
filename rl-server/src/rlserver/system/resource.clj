(ns rlserver.system.resource)

;(defn spend [game id res amount]
;  (update-in game [res id] - amount))
;
;(defn replenish [game id res amount]
;  (update-in game [res id] + amount))

(defn replenish [[now limit] amount]
  [(min (+ now amount) limit) limit])

(defn spend [[now limit] amount]
  [(max (- now amount) 0) limit])