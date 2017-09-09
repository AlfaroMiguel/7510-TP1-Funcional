(ns logical-interpreter
  (:require [clojure.test :refer :all]
    :use [database :only [generate-database]]
    :use [facts :only [exist-fact evaluate-fact is-valid-fact?]]
    :use [rules :only [exist-rule evaluate-rule]])
  )

(defn evaluate-query
  "Returns true if the rules and facts in database imply query, false if not. If
  either input can't be parsed, returns nil"
  [database query]
  (let [database (generate-database database)]
    (cond
      (or (= nil database) (not (is-valid-fact? query))) nil
      (exist-fact database query) (evaluate-fact database query)
      (exist-rule database query) (evaluate-rule database query)
      )))
