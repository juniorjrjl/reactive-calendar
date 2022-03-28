db.createUser(
    {
        user: "reactive-calendar",
        pwd: "reactive-calendar",
        roles:[
            {
                role: "readWrite",
                db:   "reactive-calendar"
            }
        ]
    }
);