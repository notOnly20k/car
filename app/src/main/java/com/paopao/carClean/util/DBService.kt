package com.paopao.carClean.util

import com.paopao.carClean.bean.*
import com.whynuttalk.foreignteacher.ext.e
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*

class DBService private constructor() {
    private var conn: Connection? = null //打开数据库对象
    private var ps: PreparedStatement? = null//操作整合sql语句的对象
    private var rs: ResultSet? = null//查询结果的集合
    private val logger=LoggerFactory.getLogger("DBService")

    companion object {

        //DBService 对象
        var dbService: DBService? = null
        get() {
            if (field==null){
                field=DBService()
            }

            return field
        }

        @Synchronized
        fun get(): DBService{
            return dbService!!
        }
    }

    fun getGoodsData(): Observable<List<News>> {
        return Observable.just("")
                .map<List<News>> {
                    val list = ArrayList<News>()
                    conn = DBOpenHelper.conn!!
                    val sql = "SELECT * FROM news "
                    ps = conn!!.prepareStatement(sql)
                    if (ps != null) {
                        rs = ps!!.executeQuery()
                        while (rs!!.next()) {
                            logger.e { rs!!.getDate("date") }
                            list.add(News(rs!!.getInt("id"),rs!!.getTimestamp("date"),
                                    rs!!.getString("content"),rs!!.getString("title"),rs!!.getString("pic")))
                        }
                    }
                    DBOpenHelper.closeAll(conn, ps)
                    list
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    ArrayList()
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun getSingleGoodsData(id:Int): Observable<Optional<News>> {
        return Observable.just(id)
                .map<Optional<News>> {
                    conn = DBOpenHelper.conn!!
                    val sql = "SELECT * FROM news "
                    ps = conn!!.prepareStatement(sql)
                    if (ps != null) {
                        rs = ps!!.executeQuery()
                        if (rs!=null) {
                            return@map Optional.of(News(rs!!.getInt("id"), rs!!.getTimestamp("date"),
                                    rs!!.getString("content"), rs!!.getString("title"), rs!!.getString("pic")))
                        }
                    }
                    DBOpenHelper.closeAll(conn, ps)
                    Optional.empty()
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    Optional.empty()
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getComments(id:Int): Observable<List<Comment>> {
        return Observable.just(id)
                .map<List<Comment>> {
                    val list = ArrayList<Comment>()
                    conn = DBOpenHelper.conn!!
                    val sql = "SELECT *,user.id as userId,comment.id as commentId FROM comment inner join user where comment.userId = user.id and newsId = $id"
                    ps = conn!!.prepareStatement(sql)
                    if (ps != null) {
                        rs = ps!!.executeQuery()
                        while (rs!!.next()) {
                            val user=User(rs!!.getInt("userId"),rs!!.getString("account"),rs!!.getString("name"),rs!!.getString("pwd"))
                            val comment=Comment(rs!!.getInt("commentId"),rs!!.getString("content"),rs!!.getTimestamp("date")
                            ,user,rs!!.getInt("userId"),rs!!.getInt("newsId"))
                            list.add(comment)
                        }
                    }
                    DBOpenHelper.closeAll(conn, ps)
                    list
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    ArrayList()
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun insComments(comment: Comment): Observable<Int> {
        return Observable.just(comment)
                .map {
                    var result = -1
                    conn = DBOpenHelper.conn!!
                    val sql = "INSERT INTO comment (userId,date,newsId,content) VALUES (?,?,?,?)"
                    ps = conn!!.prepareStatement(sql)
                    ps!!.setInt(1, comment.userId)
                    ps!!.setTimestamp(2, comment.date)
                    ps!!.setInt(3, comment.newsId)
                    ps!!.setString(4, comment.content)
                    result = ps!!.executeUpdate()//返回1 执行成功
                    DBOpenHelper.closeAll(conn, ps)
                    result
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    -1
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUser(pair: Pair<String,String>): Observable<Optional<User>> {
        return Observable.just(pair)
                .map<Optional<User>> {
                    conn = DBOpenHelper.conn!!
                    val sql = "SELECT * FROM user where account = '${pair.first}' and pwd = '${pair.second}' "
                    ps = conn!!.prepareStatement(sql)
                    if (ps != null) {
                        rs = ps!!.executeQuery()
                        if (rs!=null) {
                            while (rs!!.next()) {
                                return@map Optional.of(User(rs!!.getInt("id"), rs!!.getString("account"),
                                        rs!!.getString("name"), rs!!.getString("pwd")))
                            }
                        }
                    }
                    DBOpenHelper.closeAll(conn, ps)
                    Optional.empty()
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    Optional.empty()
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserByAccount(account:String): Observable<Optional<User>> {
        return Observable.just(account)
                .map<Optional<User>> {
                    conn = DBOpenHelper.conn!!
                    val sql = "SELECT * FROM user where account = '$account' "
                    ps = conn!!.prepareStatement(sql)
                    if (ps != null) {
                        rs = ps!!.executeQuery()
                        if (rs!=null) {
                            logger.e { account }
                            while (rs!!.next()) {
                                logger.e { rs!!.getString("account") }
                                return@map Optional.of(User(rs!!.getInt("id"), rs!!.getString("account"),
                                        rs!!.getString("name"), rs!!.getString("pwd")))
                            }
                        }
                    }
                    DBOpenHelper.closeAll(conn, ps)
                    Optional.empty()
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    Optional.empty()
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun insUser(user: User): Observable<Int> {
        return Observable.just(user)
                .map {
                    var result = -1
                    conn = DBOpenHelper.conn!!
                    val sql = "INSERT INTO user (account,pwd,name) VALUES (?,?,?)"
                    ps = conn!!.prepareStatement(sql)
                    ps!!.setString(1, user.account)
                    ps!!.setString(2, user.pwd)
                    ps!!.setString(3, user.name)
                    result = ps!!.executeUpdate()//返回1 执行成功
                    DBOpenHelper.closeAll(conn, ps)
                    result
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    -1
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun updateUser(user: User): Observable<Int> {
        return Observable.just(user)
                .map {
                    var result = -1
                    conn = DBOpenHelper.conn!!
                    val sql = "update user set account=? ,pwd=?,name=? where id=?"
                    ps = conn!!.prepareStatement(sql)
                    ps!!.setString(1, user.account)
                    ps!!.setString(2, user.pwd)
                    ps!!.setString(3, user.name)
                    ps!!.setInt(4, user.id)
                    result = ps!!.executeUpdate()//返回1 执行成功
                    DBOpenHelper.closeAll(conn, ps)
                    result
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    -1
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun insCar(car: Car): Observable<Int> {
        return Observable.just(car)
                .map {
                    var result = -1
                    conn = DBOpenHelper.conn!!
                    val sql = "INSERT INTO car (name,type,date,userId) VALUES (?,?,?,?)"
                    ps = conn!!.prepareStatement(sql)
                    ps!!.setString(1, car.name)
                    ps!!.setString(2, car.type)
                    ps!!.setTimestamp(3, car.date)
                    ps!!.setInt(4, car.userId)
                    result = ps!!.executeUpdate()//返回1 执行成功
                    DBOpenHelper.closeAll(conn, ps)
                    result
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    -1
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateCar(car: Car): Observable<Int> {
        return Observable.just(car)
                .map {
                    var result = -1
                    conn = DBOpenHelper.conn!!
                    logger.e { car }
                    val sql = "update car set name=? ,type=?,date=? where id=?"
                    ps = conn!!.prepareStatement(sql)
                    ps!!.setString(1, car.name)
                    ps!!.setString(2, car.type)
                    ps!!.setTimestamp(3, car.date)
                    ps!!.setInt(4, car.id)
                    result = ps!!.executeUpdate()//返回1 执行成功
                    DBOpenHelper.closeAll(conn, ps)
                    result
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    -1
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMyCars(id: Int): Observable<List<Car>> {
        return Observable.just(id)
                .map<List<Car>> {
                    val list = ArrayList<Car>()
                    conn = DBOpenHelper.conn!!
                    val sql = "SELECT * FROM car where userId = $id"
                    ps = conn!!.prepareStatement(sql)
                    if (ps != null) {
                        rs = ps!!.executeQuery()
                        while (rs!!.next()) {
                            logger.e { rs!!.getDate("date") }
                            list.add(Car(rs!!.getInt("id"),rs!!.getString("name"),rs!!.getTimestamp("date")
                            ,rs!!.getString("type"),rs!!.getInt("userId")))
                        }
                    }
                    DBOpenHelper.closeAll(conn, ps)
                    list
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    ArrayList()
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteCar(id: Int): Observable<Int> {
        return Observable.just(id)
                .map {
                    var result = -1
                    conn = DBOpenHelper.conn!!
                    val sql = "DELETE from car WHERE id=?"
                    ps = conn!!.prepareStatement(sql)
                    ps!!.setInt(1, id)
                    result = ps!!.executeUpdate()//返回1 执行成功
                    DBOpenHelper.closeAll(conn, ps)
                    result
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    -1
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCarCleanData(): Observable<List<CleanType>> {
        return Observable.just("")
                .map<List<CleanType>> {
                    val list = ArrayList<CleanType>()
                    conn = DBOpenHelper.conn!!
                    val sql = "SELECT * FROM `clean_type` "
                    ps = conn!!.prepareStatement(sql)
                    if (ps != null) {
                        rs = ps!!.executeQuery()
                        while (rs!!.next()) {
                            logger.e { rs!!.getString("name") }
                            logger.e { rs!!.getString("money") }
                            list.add(CleanType(rs!!.getInt("id"),rs!!.getString("name"),
                                    rs!!.getString("money"),rs!!.getString("describe"),rs!!.getString("pic")))
                        }
                    }
                    DBOpenHelper.closeAll(conn, ps)
                    list
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    ArrayList()
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun insCarOrder(carOrder: CarOrder): Observable<Int> {
        return Observable.just(carOrder)
                .map {
                    var result = -1
                    conn = DBOpenHelper.conn!!
                    val sql = "INSERT INTO car_order (clean_type,price,date,carId,userId) VALUES (?,?,?,?,?)"
                    ps = conn!!.prepareStatement(sql)
                    ps!!.setString(1, it.cleanType)
                    ps!!.setString(2, it.price)
                    ps!!.setTimestamp(3, it.date)
                    ps!!.setInt(4, it.carId)
                    ps!!.setInt(5, it.userId)
                    result = ps!!.executeUpdate()//返回1 执行成功
                    DBOpenHelper.closeAll(conn, ps)
                    result
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    -1
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getOrders(id:Int): Observable<List<CarOrder>> {
        return Observable.just(id)
                .map<List<CarOrder>> {
                    val list = ArrayList<CarOrder>()
                    conn = DBOpenHelper.conn!!
                    val sql = "SELECT car_order.*,car.name as carName FROM car_order inner join car where car_order.carId = car.id and car_order.userId = '$id'"
                    ps = conn!!.prepareStatement(sql)
                    if (ps != null) {
                        rs = ps!!.executeQuery()
                        while (rs!!.next()) {
                            val carOrder=CarOrder(rs!!.getInt("id"),rs!!.getString("clean_type"),rs!!.getString("price")
                                    ,rs!!.getTimestamp("date")
                                    ,rs!!.getInt("userId"),rs!!.getInt("carId"),rs!!.getString("carName"))
                            list.add(carOrder)
                        }
                    }
                    DBOpenHelper.closeAll(conn, ps)
                    list
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    ArrayList()
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteOrder(id: Int): Observable<Int> {
        return Observable.just(id)
                .map {
                    var result = -1
                    conn = DBOpenHelper.conn!!
                    val sql = "DELETE from car_order WHERE id=?"
                    ps = conn!!.prepareStatement(sql)
                    ps!!.setInt(1, id)
                    result = ps!!.executeUpdate()//返回1 执行成功
                    DBOpenHelper.closeAll(conn, ps)
                    result
                }.onErrorReturn { throwable ->
                    throwable.printStackTrace()
                    DBOpenHelper.closeAll(conn, ps)
                    -1
                } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }




}
