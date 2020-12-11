package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity (
    @PrimaryKey val book_id: Int,
    @ColumnInfo val bookName: String,
    @ColumnInfo val bookAuthor: String,
    @ColumnInfo val bookPrice: String,
    @ColumnInfo val bookRating: String,
    @ColumnInfo val bookDesc: String,
    @ColumnInfo val bookImage: String

)