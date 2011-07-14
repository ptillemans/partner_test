(ns partner_test.postgres
  (:use clojure.contrib.sql))

(defn valid-wafermaps [& {:keys [lotname hostname port database user password]
                          :or {hostname "postgresql-uat.colo.elex.be"
                               port 5432
                               database "inkless"
                               user "jboss"
                               password "jboss"}}]

  (let [query (str "select l.lotnumber as lotname, l.item as item, w.wafernumber as wafer, wmap.filename as filename, wmap.wafermap as wafermap
                    from wafermap wmap
                           left outer join validationerror v on v.wafermap_id = wmap.id,
                         wafer w,
                         lot l
                    where w.lot_id = l.id
                          and wmap.wafer_id = w.id
                          and l.lotnumber = '" lotname "'
                    group by l.lotnumber, l.item, w.wafernumber, wmap.id, wmap.filename, wmap.wafermap
                    having count(v.*) = 0
                    order by lotnumber, wafernumber")
        db {:classname "org.postgresql.Driver"
            :subprotocol "postgresql"
            :subname (str "//" hostname ":" port "/" database)
            :user user
            :password password}]
    (with-connection db
      (with-query-results rs [query]
        (doall rs)))))

