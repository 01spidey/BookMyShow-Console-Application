package com.company;


import java.awt.*;
import java.net.URI;
import java.util.*;
import java.util.List;

class Movie{
    String movieName;
    String movieURL;
    List<Theater> theaterList = new ArrayList<Theater>();
    Scanner scn = new Scanner(System.in);

    Movie(String name, String movieURL){
        movieName = name;
        this.movieURL = movieURL;
    }

    void viewTheaters(){
        for(int i=0;i<theaterList.size();i++){
            System.out.println((i+1)+". "+theaterList.get(i).name);
        }
        System.out.println((theaterList.size()+1)+". Go Back\n\nChoose a Theater : ");
        int op = scn.nextInt();
        System.out.println("\nYou Selected "+theaterList.get(op-1).name+" !!");
    }

    void displayDetails(){
        System.out.println("\nMovie : "+movieName);
        System.out.println("Synopsis : ");
    }

}

class Theater{
    String name;
    String location;
    List<Screen> screen_lst;

    Theater(String name, String location){
        this.name = name;
        this.location = location;
    }

    void addScreens(List<Screen> screens){
        screen_lst = screens;
        for(Screen screen:screen_lst) screen.movie.theaterList.add(this);
    }

}

class Screen{
    int totSeats = 25;
    String screen_name;
    Movie movie;
    HashMap<String, Show> show_map = new HashMap<String, Show>();


    Screen(String screen_name, Movie movie){
        this.screen_name = screen_name;
        this.movie = movie;
    }

    void addShows(String timings){
        String[] timings_arr = timings.split(" ");
        for (String s : timings_arr) show_map.put(s, new Show(s,movie));
    }

}

class Show{
    int seatsAvailable = 50;
    Movie movie;
    String showTime;
    int[][] seat_grid = new int[5][5];

    Show(String showTime,Movie movie){
        this.movie = movie;
        this.showTime = showTime;
        initSeatGrid();
    }

    void initSeatGrid(){
        int a = 1;
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                seat_grid[i][j] = a++;
            }
        }
    }

    void showSeatGrid(){
        int a = 0;
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                System.out.println(a+" ");
            }
            System.out.println();
        }
    }

}

class User{
    String name, pass, contact;
    User(String name, String pass, String contact){
        this.name = name;
        this.pass = pass;
        this.contact = contact;
    }

}



public class BookMyShow {

    static HashMap<String,Theater> theater_map = new LinkedHashMap<String, Theater>();
    static HashMap<String,Movie> movie_map = new LinkedHashMap<String, Movie>();
    static HashMap<String, User> user_map = new LinkedHashMap<String, User>();

    static Scanner scn = new Scanner(System.in);

    static {
        Screen screen;
        ArrayList<Screen> screen_lst = new ArrayList<Screen>();

        Theater theater_1 = new Theater("Sri Shakti Cinemas","Tiruppur");

        String show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";

        screen = new Screen("Screen-1", movie_map.containsKey("Ponniyin Selvan-1")?movie_map.get("Ponniyin Selvan-1"):new Movie("Ponniyin Selvan-1","https://www.imdb.com/title/tt10701074/?ref_=ttpl_ov_i"));
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-2", movie_map.containsKey("Sita Ramam")?movie_map.get("Sita Ramam"):new Movie("Sita Ramam","https://www.imdb.com/title/tt20850406/?ref_=nv_sr_srsg_0"));
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-3", movie_map.containsKey("Anbe Sivam")?movie_map.get("Anbe Sivam"):new Movie("Anbe Sivam","https://www.imdb.com/title/tt0367495/?ref_=nv_sr_srsg_0"));
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-4", movie_map.containsKey("Aandavan Kattalai")?movie_map.get("Aandavan Kattalai"):new Movie("Aandavan Kattalai","https://www.imdb.com/title/tt6076366/?ref_=nv_sr_srsg_0"));
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        theater_1.addScreens(screen_lst);

        theater_map.put(theater_1.name, theater_1);

//        ---------------------------------------------------------------------------------------------------------------------------------------------
        screen_lst.clear();

        Theater theater_2 = new Theater("Srinivasa Cinemas","Tiruppur");

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-1", movie_map.containsKey("Madarasapattinam")?movie_map.get("Madarasapattinam"):new Movie("Madarasapattinam","https://www.imdb.com/title/tt1365030/?ref_=nv_sr_srsg_0"));
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-2", movie_map.containsKey("Jai Bhim")?movie_map.get("Jai Bhim"):new Movie("Jai Bhim","https://www.imdb.com/title/tt15097216/?ref_=nv_sr_srsg_0"));
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-3", movie_map.containsKey("Enthiran")?movie_map.get("Enthiran"):new Movie("Enthiran","https://www.imdb.com/title/tt1305797/?ref_=nv_sr_srsg_2"));
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put(screen.movie.movieName,screen.movie);

        show_timings = "10:30 AM-02:15 PM-06:15 PM-10:00 PM";
        screen = new Screen("Screen-4", movie_map.containsKey("Ponniyin Selvan-1")?movie_map.get("Ponniyin Selvan-1"):new Movie("Ponniyin Selvan-1","https://www.imdb.com/title/tt10701074/?ref_=ttpl_ov_i"));
        screen.addShows(show_timings);
        screen_lst.add(screen);
        movie_map.put((screen.movie).movieName,screen.movie);

        theater_2.addScreens(screen_lst);

        theater_map.put(theater_2.name, theater_2);

    }

    static String userSignUp(String name, String pass, String contact){
        User user = new User(name,pass,contact);
        user_map.put(name+"-"+pass,user);
        return name+"-"+pass;
    }

    static String askFirst(){
        System.out.println("\n---------- Book My Shows !! ----------");
        System.out.print("1. Sign Up\n2. Sign In\n3. Exit\n\nChoose an Option : ");
        int op = scn.nextInt();

        if(op==1){
            System.out.println("\n---------- User Sign Up ----------");
            System.out.print("Name : ");
            scn.nextLine();
            String name = scn.nextLine();
            System.out.print("Password : ");
            String pass = scn.nextLine();
            if(user_map.containsKey(name+"-"+pass)){
                System.out.println("\nUsername Not Available !!");
                return "null";
            }
            System.out.print("Contact : +91 ");
            String contact = scn.nextLine();
            return userSignUp(name,pass,contact);
        }
        else if(op==2){
            System.out.println("\n---------- User Sign In ----------");
            System.out.print("Name : ");
            scn.nextLine();
            String name = scn.nextLine();
            System.out.print("Password : ");
            String pass = scn.nextLine();
            if(user_map.containsKey(name+"-"+pass)) return name+"-"+pass;
            else{
                System.out.println("\nUser Not Found !!");
                return "null";
            }
        }
        else return "exit";

    }

    static void visitPage(String url){
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI uri = new java.net.URI(url);
            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        String cur_login = "";

        while(true){
            cur_login = askFirst();

            if(cur_login.equals("exit")){
                System.out.println("\nShutting the System Down !!");
                break;
            }
            else if(!cur_login.equals("null")){
                User user = user_map.get(cur_login);
                System.out.println("\nWelcome "+user.name+" !!");
                boolean out_flag = true;
                while(out_flag){
                    System.out.println("\nWondering What to Watch?! ...");
                    List<String> movies = new ArrayList<String>(movie_map.keySet());
                    for (int i = 0; i < movies.size(); i++) System.out.println((i + 1) + ". " + movies.get(i));
                    System.out.print((movies.size() + 1) + ". Go Back\n\nChoose Something to watch : ");
                    int op = scn.nextInt();

                    if (op <= movies.size()) {
                        boolean flag = true;
                        while (flag) {
                            Movie movie = movie_map.get(movies.get(op - 1));
                            System.out.println("\nMovie : " + movie.movieName);
                            System.out.print("1. About Movie\n2. Book Ticket\n3. Go Back\n\nChoose an Action : ");
                            int b = scn.nextInt();

                            if (b == 1) {
                                System.out.println("Redirecting to IMDB...");
                                visitPage(movie.movieURL);
                            }
                            else if (b == 2) {
                                boolean flag_2 = true;
                                while (flag_2) {
                                    System.out.println("\n---------- Running On ----------");
                                    List<Theater> theaters = movie.theaterList;
                                    for (int i = 0; i < theaters.size(); i++)
                                        System.out.println((i + 1) + ". " + theaters.get(i).name);
                                    System.out.print((theaters.size() + 1) + ". " + "Go Back\n\nChoose a Theater : ");
                                    int ch = scn.nextInt();
                                    if (ch <= theaters.size()) {
                                        System.out.println("\nYou Selected " + theaters.get(ch - 1).name);
                                    } else flag_2 = false;
                                }
                            } else flag = false;
                        }
                    } else out_flag = false;
                }
            }
        }


    }
}
