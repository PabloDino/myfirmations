package com.example.myfirmations.data


class DefaultData {

    fun loadSettings(): Settings {
        return Settings(0,15f,true, "US Female")

    }

    fun loadFirmations(): List<Firmation> {
        return listOf<Firmation>(
            Firmation(0, "Work for everything - Be entitled to nothing.", "birds", true, 0),
            Firmation(0, "If you lose - Don\'t lose the lesson.", "riversea", true, 0),
            Firmation(0,"Things are harder than they need to be - not to worry - you are tougher than you need to be.", "path", true, 0),
            Firmation(0,"Smooth seas do not make skillful sailors.", "mountain", true, 0),
            Firmation(0,"Perfect practice builds perfection", "boat", true, 0),
            Firmation(0,"Today\'s pain is tomorrow\'s strength.", "mountain", true, 0),
            Firmation(0,"You do not get to where you are going, by remaining where you are.", "riversea", true, 0),
            Firmation(0,"When all doors close around you, it is time to make one.", "bridge", true, 0),
            Firmation(0,"Be the master of what drives you... not the other way around.", "hammock", true, 0),
            Firmation(0,"If it was easy, everyone would have already done it.", "highbeach", true, 0),

            Firmation(0,"If others do not believe in you, it becomes that more important to believe in yourself.", "path", true, 0),
            Firmation(0,"When you think you are going through hell, DON\'T STOP!!!.", "path", true, 0),
            Firmation(0,"Darkness will not overcome darkness - Only light will do that. Hate will not overcome hate - Only love will do that.", "sunrise", true, 0),
            Firmation(0,"It\'s ok to take a breath. The world will still be here when you are done.", "hammock", true, 0),
            Firmation(0,"Do what you can... Don\'t \'not do\' what you can\'t.", "bridge", true, 0),
            Firmation(0,"Earn from what you love, so you don\'t need to work a day in your life.", "hammock", true, 0),
            Firmation(0,"Good training is not about being able to get it right... It\'s about not being able to get it wrong.", "highbeach", true, 0),
            Firmation(0,"Adversity creates strength.", "mountain", true, 0),
            Firmation(0,"Bravery is not about having no fear. It\'s about facing fears.", "path", true, 0),
            Firmation(0,"Feeling sorry for yourself is the root of all evil.", "predawn", true, 0),
            Firmation(0,"Time tells the truth.", "mountain", true, 0),
            Firmation(0,"Your plans, hopes and dreams are just that. YOURS!!!.", "birds", true, 0),
            Firmation(0,"Every breath is a new opportunity.", "sunrise", true, 0),
            Firmation(0,"Stay strong because you ARE the shield.", "highbeach", true, 0),
            Firmation(0,"Weakness is for those who can afford it.", "predawn", true, 0),
            Firmation(0,"When the goal is very far, one step closer is often good enough.", "riversea", true, 0),
            Firmation(0,"Everyone has a superpower. Learn yours.", "birds", true, 0),
            Firmation(0,"Diamonds are created by pressure, which is why you are tough.", "highbeach", true, 0),
            Firmation(0,"Never stop learning.", "birds", true, 0),
            Firmation(0,"Technique counters strength.", "bridge", true, 0),
            Firmation(0,"Timing counters speed.", "hammock", true, 0),
            Firmation(0,"Movement counters size.", "birds", true, 0),
            Firmation(0,"Let every failure sharpen your resolve until you can cut through any barrier.", "highbeach", true, 0),
            Firmation(0,"Time heals.", "predawn", true, 0),
            Firmation(0,"Inner peace does not worry about outside noise.", "predawn", true, 0),
            Firmation(0,"Don\'t be afraid of mistakes .. Good Decisions come from experience, and experience comes from mistakes.", "boat", true, 0),
            Firmation(0,"Perfect practice builds perfection", "boat", true, 0),
            Firmation(0,"Whoever said it was easy probably shouldn\'t be trusted.", "mountain", true, 0),
            Firmation(0,"Every now and again, enjoy the view.", "sunrise", true, 0),
            Firmation(0,"A weakness that matters should be your next strength.", "bridge", true, 0),
            Firmation(0,"It may be hard to stick to it, but can you respect yourself if you don\'t?", "predawn", true, 0),
            Firmation(0,"Not everything precious glitters.", "hammock", true, 0),
            Firmation(0,"A difficulty is simply a challenge yet to be mastered.", "boat", true, 0),
            Firmation(0,"One one coco full basket.", "riversea", true, 0),

            Firmation(0,"Once you break the barriers, they will remember your name.", "bridge", true, 0),
            Firmation(0,"Home is where the heart is.", "boat", true, 0),
            Firmation(0,"Every challenge is another opportunity.", "sunrise", true, 0),
            Firmation(0,"Today is the first day of the rest of your life.", "sunrise", true, 0),
            Firmation(0,"When all is said and done, what you said should be done.", "riversea", true, 0),
            Firmation(0,"When the pain is too great, laugh.", "path", true, 0),

            ).shuffled()
    }
}


