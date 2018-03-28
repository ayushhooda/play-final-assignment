package model

case class User(id: Int, fname: String, mname: String, lname: String, email: String,
                password: String, mobile: String, gender: String, age: Int, hobbies: String,
                isEnabled: Boolean, isAdmin: Boolean)
