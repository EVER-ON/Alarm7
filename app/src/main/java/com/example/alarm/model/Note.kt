package com.example.alarm.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
public class Note : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    @ColumnInfo(name = "text")
    var text: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0

    @ColumnInfo(name = "done")
    var done = false

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val note = o as Note
        if (uid != note.uid) return false
        if (timestamp != note.timestamp) return false
        if (done != note.done) return false
        return if (text != null) text == note.text else note.text == null
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + if (text != null) text.hashCode() else 0
        result = 31 * result + (timestamp xor (timestamp ushr 32)).toInt()
        result = 31 * result + if (done) 1 else 0
        return result
    }

    protected constructor(`in`: Parcel) {
        uid = `in`.readInt()
        text = `in`.readString()
        timestamp = `in`.readLong()
        done = `in`.readByte().toInt() != 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(uid)
        dest.writeString(text)
        dest.writeLong(timestamp)
        dest.writeByte((if (done) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Note?> = object : Parcelable.Creator<Note?> {
            override fun createFromParcel(`in`: Parcel): Note? {
                return Note(`in`)
            }

            override fun newArray(size: Int): Array<Note?> {
                return arrayOfNulls(size)
            }
        }
    }
}