package com.example.warrioroop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.math.ceil
import kotlin.random.Random as Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val w1dice = Dice(20)
        val w1 = Warrior("JACK",100,20,10,w1dice)

        val w2dice = Dice(20)
        val w2 = Warrior("KEVIN",100,20,10,w2dice)

        val arena = Arena(w1,w2)

        arena.template()
        arena.fight()
    }
}

class Arena(val warrior1: Warrior,val warrior2: Warrior)
{
    fun template()
    {
        println("\n" +
                "    _     ____ _____ _   _    _    \n" +
                "   / \\   / _  |____ | | / |  / \\   \n" +
                "  / _ \\ | (_| | |_  | |/  | / _ \\  \n" +
                " / ___ \\ > _  |___| |  /| |/ ___ \\ \n" +
                "/_/   \\_/_/ |_|_____|_/ |_/_/   \\_\\\n" +
                "                                   \n")
        println("WARIOR1 :")
        println("Name : ${warrior1.name} Health : ${warrior1.maxHealth} Power : ${warrior1.damage} Defence : ${warrior1.defence}")
        println("WARIOR2 :")
        println("Name : ${warrior2.name} Health : ${warrior2.maxHealth} Power : ${warrior2.damage} Defence : ${warrior2.defence}")
    }
    fun fight()
    {
        while(warrior1.WarriorAlive() && warrior2.WarriorAlive())
        {
            warrior1.Attack(warrior2)
            println("${warrior1.getLastMessage()}")
            println("${warrior2.getLastMessage()}")
            println("${warrior1.name} (Health ${warrior1.health}):${warrior1.HealthBar()}")
            println("${warrior2.name} (Health ${warrior2.health}):${warrior2.HealthBar()}")

            if(warrior2.WarriorAlive())
            {
                warrior2.Attack(warrior1)
                println("${warrior2.getLastMessage()}")
                println("${warrior1.getLastMessage()}")
                println("${warrior1.name} (Health ${warrior1.health}):${warrior1.HealthBar()}")
                println("${warrior2.name} (Health ${warrior2.health}):${warrior2.HealthBar()}")
            }
        }
    }
}

class Dice(side :Int)
{
    var DiceSideSize : Int? = null

    init {
        DiceSideSize = side
    }
    constructor(): this(6)
    
    fun RollDice() : Int
    {
        val result = Random.nextInt(1,DiceSideSize!!)
        return result
    }

    override fun toString(): String {
        return "The Dice Has $DiceSideSize Side"
    }

}
class Warrior(name : String, health : Int, damage : Int, defence : Int, dice : Dice)
{
    var name : String? = null
    var health : Int? = null
    var damage : Int? = null
    var defence : Int? = null
    var dice : Dice? = null
    var maxHealth : Int? = null
    var message : String? = null

    init {
        this.name = name
        this.health = health
        this.damage = damage
        this.defence = defence
        this.dice = dice

        this.maxHealth = health
    }
    fun setupMessage(lastMessage : String)
    {
        this.message = lastMessage
    }

    fun getLastMessage() : String?
    {
        return this.message
    }

    fun WarriorAlive() :Boolean
    {
        return (this.health!! <= 0).not()
    }

    fun HealthBar() : String
    {
        val total : Int = 20
        val piece : Int = (this.maxHealth!!/total)
        var bar : String = "["
        var border : Int = 0
        if(WarriorAlive())
        {
            if ((this.health!!/piece +1) < piece && (this.health!!/piece +1) != 0)
            {
                border = (this.health!!/piece +1) + 1
            }
            else
            {
                border = (this.health!!/piece +1)
            }
            bar = bar.padEnd(border,'█')
            bar = bar.padEnd(21,'░')
        }
        else {
            bar = bar.padEnd(22,'░')
        }
        return "$bar]"
    }

    fun Defend(attack : Int)
    {
        val injury : Int = attack - (this.defence!! + dice!!.RollDice())

        if(injury > 0) {
            if (injury >= this.health!!) {
                this.health = 0
                setupMessage("${this.name} warrior hits the deadly spot with damage of  ${injury}!!!!!!!")
            } else {
                this.health = this.health!! - injury
                setupMessage("${this.name} warrior takes ${injury} damage from attack")
            }
        }
        else
        {
            setupMessage("${this.name} blocks the attacks easily.")
        }
    }

    fun Attack(enemyWarrior: Warrior)
    {
        val hit = this.damage!! + dice!!.RollDice()
        setupMessage("${this.name} warrior attacks to the  ${enemyWarrior.name} by using sword and damage of  ${hit}!!")
        enemyWarrior.Defend(hit)
    }
}