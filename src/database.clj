(ns database
  (:use [facts :only [is-valid-fact?]]
    :use [rules :only [is-valid-rule?]])
  )

(defn parse-raw-database
  "Given a raw database as a string, returns a set of facts and/or generics rules"
  [raw-database]
  (set
   (filter
    (fn [x] (not (clojure.string/blank? x) ))
    (map
     (fn [string] (clojure.string/trim string) )
     (clojure.string/split raw-database  #"\.")
     ))))

(defn is-valid-database?
  "Checks if the database contains valid rules or facts. Otherwise return false"
  [database]
  (reduce =
          (map (fn [x] (or (is-valid-fact? x) (is-valid-rule? x))) database)))

(defn generate-database
  "Given a raw database as a string, returns an ArrayMap with all the facts and rules"
  [raw-database]
  (let [parsed-database (parse-raw-database raw-database)]
    (if (not (is-valid-database? parsed-database)) nil
      {
        :facts (filter is-valid-fact? parsed-database),
        :rules (filter is-valid-rule? parsed-database)
        }
      )))

