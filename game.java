import java.awt.*;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import tester.Tester;
import javalib.worldimages.*;   
import javalib.funworld.*;      




//represents the element in the game
abstract class AElements {
  int x;
  int y;
  int radius;
  Color color;
  int speedX;
  int speedY;
  
  AElements(int x, int y, int radius, Color color, int speedX, int speedY) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.color = color;
    this.speedX = speedX;
    this.speedY = speedY;
  }
  
  /* TEMPLATE
     Fields:
     ... this.x ...                   --int
     ... this.y ...                   --int
     ... this.radius ...              --int
     ... this.color ...               --Color
     ... this.speedX ...              --int
     ... this.speedY ...              --int
     Methods:
     ... this.drawCircle() ...        --WorldImage
     ... this.radians(int theta) ...  --double
     ... this.rotate(int now) ...     --Bullet
     ... this.rotateList(int acc) ... --IList<Bullet>
     ... this.randomShip() ...        --Ship
   */
  

  
  //draw the element
  WorldImage drawCircle() {
    return new CircleImage(this.radius, OutlineMode.SOLID, this.color);
  }
  
  //trans the theta to the radians
  double radians(int theta) {
    return (theta * Math.PI / 180);
  }
  
  //show the circle when a Bullet Split
  abstract Bullet rotate(int now);
  
  //show the list of split bullets
  abstract IList<Bullet> rotateList(int acc);
  
  //generate a random ship
  abstract Ship randomShip();
}

//to create the Bullet
class Bullet extends AElements {
  int boom;
  
  Bullet(int x, int y, int radius, Color color, int speedX, int speedY, int boom) {
    super(x, y, radius, color, speedX, speedY);
    this.boom = boom;
  }
  
  Bullet(int x, int y, int radius, int speedX, int speedY, int boom) {
    this(x, y, radius, Color.PINK, speedX, speedY, boom);
  }
  
  /* TEMPLATE
     Fields:
     ... this.x ...                   --int
     ... this.y ...                   --int
     ... this.radius ...              --int
     ... this.color ...               --Color
     ... this.speedX ...              --int
     ... this.speedY ...              --int
     ... this.boom ...                --int
     Methods:
     ... this.drawCircle() ...        --WorldImage
     ... this.radians(int theta) ...  --double
     ... this.rotate(int now) ...     --Bullet
     ... this.rotateList(int acc) ... --IList<Bullet>
     ... this.randomShip() ...        --Ship
    */

  //show the circle when a Bullet Split  
  public Bullet rotate(int now) {
    int newBoom = this.boom + 1;
    if (newBoom > 3) {
      return new Bullet(this.x, this.y, 10, this.color, (int)(8 * Math.cos(radians(
          now * (360 / (newBoom + 1))))), (int)(0 - (8 * Math.sin(radians(
              now * (360 / (newBoom + 1)))))), newBoom);
    } else {
      return new Bullet(this.x, this.y, 2 + 2 * newBoom, this.color, (int)(8 * Math.cos(radians(
          now * (360 / (newBoom + 1))))), (int)(0 - (8 * Math.sin(radians(
              now * (360 / (newBoom + 1)))))), newBoom);
    }
  }
  
  //show the rotate Bullet list
  public IList<Bullet> rotateList(int acc) {
    if (acc > 0) {
      return new ConsList<Bullet>(this.rotate(acc), rotateList(acc - 1));
    } else {
      return new ConsList<Bullet>(this.rotate(acc), new MtList<Bullet>()); 
    }
  }
  
  //generate a random ship
  public Ship randomShip() {
    return null;
  }
}

//to create the Ship
class Ship extends AElements {
  
  Ship(int x, int y, int radius, Color color, int speedX, int speedY) {
    super(x, y, radius, color, speedX, speedY);
  }
  
  Ship(int x, int y, int radius, int speedX) {
    this(x, y, radius, Color.CYAN, speedX, 0);
  }
  
  /* TEMPLATE
     Fields:
     ... this.x ...                   --int
     ... this.y ...                   --int
     ... this.radius ...              --int
     ... this.color ...               --Color
     ... this.speedX ...              --int
     ... this.speedY ...              --int
     Methods:
     ... this.drawCircle() ...        --WorldImage
     ... this.radians(int theta) ...  --double
     ... this.rotate(int now) ...     --Bullet
     ... this.rotateList(int acc) ... --IList<Bullet>
     ... this.randomShip() ...        --Ship
    */  
  

  
  //show the circle when the Bullet Split
  public Bullet rotate(int now) {
    return null;
  }
  
  //show the list of rotated bullet
  public IList<Bullet> rotateList(int acc) {
    return null;
  }
  
  //generate a random ship
  public Ship randomShip() {
    Random r = new Random();
    
    if (r.nextInt(2) == 1) {
      return new Ship(0 - this.radius, r.nextInt(300 * 5 / 7) 
          + (int)(300 / 7), this.radius, this.color, 4, 0);
    } else {
      return new Ship(500 + this.radius, r.nextInt(300 * 5 / 7)
          + (int)(300 / 7), this.radius, this.color, -4, 0);
    }
  }
}


//make a interface which take two parameter and make a decision
interface Predicate2<T, U> {
  boolean test(T t, U u);
}

//make the IList of any type
interface IList<T> {
  //delete any element in the list which do not fit the predicate
  IList<T> filter(Predicate<T> pred);
  
  //delete any element in the list which do not fit the predicate
  <U> IList<T> filter2(Predicate2<T, U> pred, U compare);
  
  //change the element in the list according to converter
  <U> IList<U> map(Function<T,U> converter);
  
  //checks if everything in this list passes the predicate
  boolean ormap(Predicate<T> pred);
  
  //checks if everything in this list passes the predicate
  <U> boolean ormap2(Predicate2<T, U> pred, U compare);
  
  //get a result through the list according to converter
  <U> U fold(BiFunction<T,U,U> converter,U initial);  
}

//the empty list of any type
class MtList<T> implements IList<T> {
  
  MtList() {}

  //delete any element in the list which do not fit the predicate
  public IList<T> filter(Predicate<T> pred) {
    return new MtList<T>();
  }

  //delete any element in the list which do not fit the predicate
  public <U> IList<T> filter2(Predicate2<T, U> pred, U compare) {
    return new MtList<T>();
  }
  
  //change the element in the list according to converter
  public <U> IList<U> map(Function<T, U> converter) {
    return new MtList<U>();
  }

  
  //checks if everything in this list passes the predicate
  public boolean ormap(Predicate<T> pred) {
    return false;
  }
  
  //checks if everything in this list passes the predicate
  public <U> boolean ormap2(Predicate2<T, U> pred, U compare) {
    return false;
  }
  
  //get a result through the list according to converter
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return initial;
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  ConsList(T first,IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  /* TEMPLATE
     Fields:
     ... this.first ...                                           --T
     ... this.rest ...                                            --IList<T>
     Methods:
     ... this.filter(Predicate<T> pred) ...                       --IList<T>
     ... this.filter2(Predicate2<T, U> pred, U compare) ...       --IList<T>
     ... this.map(Function<T, U> converter) ...                   --IList<U>
     ... this.ormap(Predicate<T> pred) ...                        --boolean
     ... this.ormap2(Predicate2<T, U> pred, U compare) ...        --boolean
     ... this.fold(BiFunction<T, U, U> converter, U initial) ...  --U
   */
  
  //delete any element in the list which do not fit the predicate
  public IList<T> filter(Predicate<T> pred) {
    if (pred.test(this.first)) {
      return new ConsList<T>(this.first,this.rest.filter(pred));
    } else {
      return this.rest.filter(pred);
    }
  }
  
  //delete any element in the list which do not fit the predicate
  public <U> IList<T> filter2(Predicate2<T, U> pred, U compare) {
    if (pred.test(this.first, compare)) {
      return new ConsList<T>(this.first, this.rest.filter2(pred, compare));      
    } else {
      return this.rest.filter2(pred, compare);
    }
  }
  
  //change the element in the list according to converter
  public <U> IList<U> map(Function<T, U> converter) {
    return new ConsList<U>(converter.apply(this.first),this.rest.map(converter));
  }
  
  
  //checks if everything in this list passes the predicate
  public boolean ormap(Predicate<T> pred) {
    return pred.test(this.first) || this.rest.ormap(pred);
  }
  
  //checks if everything in this list passes the predicate
  public <U> boolean ormap2(Predicate2<T, U> pred, U compare) {
    return pred.test(this.first, compare) || this.rest.ormap2(pred, compare);
  }

  //get a result through the list according to converter
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return converter.apply(this.first, this.rest.fold(converter,initial));
  }
}

//used on fold to calculate the length of list of Bullet
class Length1 implements BiFunction<Bullet, Integer, Integer> {
  public Integer apply(Bullet b, Integer i) {
    return 1 + i;
  }
}


//used on fold to calculate the length of list of Ship
class Length2 implements BiFunction<Ship, Integer, Integer> {
  public Integer apply(Ship s, Integer i) {
    return 1 + i;
  }
}

//used on fold to draw Bullet on worldScene
class DrawBullet implements BiFunction<Bullet, WorldScene, WorldScene> {
  public WorldScene apply(Bullet b, WorldScene s) {
    return s.placeImageXY(b.drawCircle(), (int)b.x, (int)b.y);
  }
}

//used on fold to draw Ship on worldScene
class DrawShip implements BiFunction<Ship, WorldScene, WorldScene> {
  public WorldScene apply(Ship sh, WorldScene s) {
    return s.placeImageXY(sh.drawCircle(), (int)sh.x, (int)sh.y);
  }
}

//used on filter to delete the outside Ship
class ShipOutside implements Predicate<Ship> {
  public boolean test(Ship s) {
    return !(s.x - 500 > s.radius
        || 0 - s.x > s.radius
        || s.y - 300 > s.radius
        || 0 - s.y > s.radius);
  }
}

//used on map to make a list of <list of Bullet>, which shows the new generated Bullet
class Split implements Function<Bullet, IList<Bullet>> {
  public IList<Bullet> apply(Bullet b) {
    return b.rotateList(b.boom + 1);
  }
}

//used on filter to delete the outside Bullet
class BulletOutside implements Predicate<Bullet> {
  public boolean test(Bullet b) {
    return !(b.x - 500 > b.radius
        || 0 - b.x > b.radius
        || b.y - 300 > b.radius
        || 0 - b.y > b.radius);
  }
}

//used on filter2 to help BulletHit2 to delete the hit Bullet
class BulletHit implements Predicate2<Bullet, Ship> {
  public boolean test(Bullet b, Ship s) {
    return Math.hypot(b.x - s.x, b.y - s.y) 
        <= (b.radius + s.radius);
  }
}

//used on filter2 to help ShipHit2 to delete the hit Ship
class ShipHit implements Predicate2<Ship, Bullet> {
  public boolean test(Ship s, Bullet b) {
    return Math.hypot(b.x - s.x, b.y - s.y) 
        <= (b.radius + s.radius);
  }
}

//used on filter2 to delete the hit Bullet
class BulletHit2 implements Predicate2<Bullet, IList<Ship>> {
  public boolean test(Bullet b, IList<Ship> i) {
    return !(i.ormap2(new ShipHit(), b));
  }
}

//used on filter2 to find the hit Bullet
class BulletHit3 implements Predicate2<Bullet, IList<Ship>> {
  public boolean test(Bullet b, IList<Ship> i) {
    return i.ormap2(new ShipHit(), b);
  }
}

//used on filter2 to delete the hit Ship
class ShipHit2 implements Predicate2<Ship, IList<Bullet>> {
  public boolean test(Ship s, IList<Bullet> i) {
    return !(i.ormap2(new BulletHit(), s));
  }
} 

//used on map to make the Bullet move
class BulletMove implements Function<Bullet, Bullet> {
  public Bullet apply(Bullet b) {
    return new Bullet(b.x + (int)b.speedX, b.y + (int)b.speedY,
      b.radius, b.color, b.speedX, b.speedY, b.boom); 
  }
}

//used on map to make the Ship move
class ShipMove implements Function<Ship, Ship> {
  public Ship apply(Ship s) {
    return new Ship(s.x + (int)s.speedX, s.y, s.radius, s.color,
        s.speedX, s.speedY);
  }
}

//used on fold to connect two lists of Bullet
class BulletAppend implements BiFunction<Bullet, IList<Bullet>, IList<Bullet>> {
  public IList<Bullet> apply(Bullet b, IList<Bullet> list) {
    return new ConsList<Bullet>(b, list);
  }
}


//used on list of <List of Bullet> to make it become list of Bullet
class BulletExpand implements BiFunction<IList<Bullet>, IList<Bullet>, IList<Bullet>> {
  public IList<Bullet> apply(IList<Bullet> i1, IList<Bullet> i2) {
    return i1.fold(new BulletAppend(), i2);
  }
}

//make the world: GameWorld
class GameWorld extends World {
  IList<Ship> listOfShips;
  IList<Bullet> listOfBullets;
  int bulletNumber;
  int timeCalculator;
  int destroyedShip;
  
  GameWorld() {
    this.listOfShips = new MtList<Ship>();
    this.listOfBullets = new MtList<Bullet>();
    this.bulletNumber = 10;
    this.timeCalculator = 28;
    this.destroyedShip = 0;
  }
  
  GameWorld(IList<Ship> listOfShips, IList<Bullet> listOfBullets,
      int bulletNumber, int timeCalculator, int destroyedShip) {
    this.listOfShips = listOfShips;
    this.listOfBullets = listOfBullets;
    this.bulletNumber = bulletNumber;
    this.timeCalculator = timeCalculator;
    this.destroyedShip = destroyedShip;
  }
  
  /* TEMPLATE
     Fields:
     ... this.listOfShips ...                                                          --IList<Ship>
     ... this.listOfBullets ...                                                        
     --IList<Bullet>
     ... this,bulletNumber ...                                                         --int
     ... this.timeCalculator ...                                                       --int
     ... this.destroyedShip ...                                                        --int
     Methods:
     ... this.makeScene() ...                                                          --WorldScene
     ... this.onTick() ...                                                             --World
     ... this.onKeyEvent(String key) ...                                               --World
     ... this.worldEnds() ...                                                          --World
     Methods Of Fields:
     ... this.listOfShips.filter(Predicate<Ship> pred) ...                             --IList<Ship>
     ... this.listOfShips.filter2(Predicate2<Ship, U> pred, U compare) ...             --IList<Ship>
     ... this.listOfShips.map(Function<Ship, U> converter) ...                         --IList<U>
     ... this.listOfShips.ormap(Predicate<Ship> pred) ...                              --boolean
     ... this.listOfShips.ormap2(Predicate2<Ship, U> pred, U compare) ...              --boolean
     ... this.listOfShips.fold(BiFunction<Ship, U, U> converter, U initial) ...        --U
     ... this.listOfBullets.filter(Predicate<Bullet> pred) ...                         
     --IList<Bullet>
     ... this.listOfBullets.filter2(Predicate2<Bullet, U> pred, U compare) ...         
     --IList<Bullet>
     ... this.listOfBullets.map(Function<Bullet, U> converter) ...                     --IList<U>
     ... this.listOfBullets.ormap(Predicate<Bullet> pred) ...                          --boolean
     ... this.listOfBullets.ormap2(Predicate2<Bullet, U> pred, U compare) ...          --boolean
     ... this.listOfBullets.fold(BiFunction<Bullet, U, U> converter, U initial) ...    --U
   */
  
  //make the WorldSence for the world
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(500, 300);
    WorldImage text = new TextImage("Bullets left" 
        + " " + Integer.toString(bulletNumber)
        + "; " + "Ships destroyed: "
        + Integer.toString(destroyedShip), 13, Color.BLACK);
    
    return this.listOfBullets.fold(new DrawBullet(),
        this.listOfShips.fold(new DrawShip(), scene)).placeImageXY(text,
             (int)(text.getWidth() / 2), (int)(300 - (text.getHeight() / 2)));
  }
  
  //refresh the world on tick
  public World onTick() {
    Random r = new Random();
    if (timeCalculator == 28) {
      if (r.nextInt(3) == 2) {
        return new GameWorld(
          new ConsList<Ship>(new Ship(1, 1, 10, Color.CYAN, 0, 0).randomShip(),
          new ConsList<Ship>(new Ship(1, 1, 10, Color.CYAN, 0, 0).randomShip(),
              new ConsList<Ship>(new Ship(1, 1, 10, Color.CYAN, 0, 0).randomShip(),
                  this.listOfShips
                  .filter2(new ShipHit2(), this.listOfBullets)
                  .map(new ShipMove())
                  .filter(new ShipOutside())))),
          this.listOfBullets
          .filter2(new BulletHit2(), this.listOfShips)
          .map(new BulletMove())
          .fold(new BulletAppend(), listOfBullets
              .filter2(new BulletHit3(), this.listOfShips)
              .map(new Split())
              .fold(new BulletExpand(), new MtList<Bullet>()))
          .filter(new BulletOutside()),
          this.bulletNumber,
          0,
          this.destroyedShip + (this.listOfShips
              .fold(new Length2(), 0) 
              - this.listOfShips
              .filter2(new ShipHit2(), this.listOfBullets)
              .fold(new Length2(), 0)));
      } else if (r.nextInt(3) == 1) {
        return new GameWorld(
          new ConsList<Ship>(new Ship(1, 1, 10, Color.CYAN, 0, 0).randomShip(),
              new ConsList<Ship>(new Ship(1, 1, 10, Color.CYAN, 0, 0).randomShip(),
                  this.listOfShips
                  .filter2(new ShipHit2(), this.listOfBullets)
                  .map(new ShipMove())
                  .filter(new ShipOutside()))),
          this.listOfBullets
          .filter2(new BulletHit2(), this.listOfShips)
          .map(new BulletMove())
          .fold(new BulletAppend(), listOfBullets
              .filter2(new BulletHit3(), this.listOfShips)
              .map(new Split())
              .fold(new BulletExpand(), new MtList<Bullet>()))
          .filter(new BulletOutside()),
          this.bulletNumber,
          0,
          this.destroyedShip 
          + (this.listOfShips
              .fold(new Length2(), 0) 
              - this.listOfShips
              .filter2(new ShipHit2(), this.listOfBullets)
              .fold(new Length2(), 0)));
      } else {
        return new GameWorld(
              new ConsList<Ship>(new Ship(1, 1, 10, Color.CYAN, 0, 0).randomShip(),
                  this.listOfShips
                  .filter2(new ShipHit2(), this.listOfBullets)
                  .map(new ShipMove())
                  .filter(new ShipOutside())),
              this.listOfBullets
              .filter2(new BulletHit2(), this.listOfShips)
              .map(new BulletMove())
              .fold(new BulletAppend(), listOfBullets
                  .filter2(new BulletHit3(), this.listOfShips)
                  .map(new Split())
                  .fold(new BulletExpand(), new MtList<Bullet>()))
              .filter(new BulletOutside()),
          this.bulletNumber,
          0,
          this.destroyedShip 
          + (this.listOfShips
              .fold(new Length2(), 0) 
              - this.listOfShips
              .filter2(new ShipHit2(), this.listOfBullets)
              .fold(new Length2(), 0)));
      }
    } else {
      return new GameWorld(this.listOfShips
        .filter2(new ShipHit2(), this.listOfBullets)
        .map(new ShipMove())
        .filter(new ShipOutside()),
        this.listOfBullets
        .filter2(new BulletHit2(), this.listOfShips)
        .map(new BulletMove())
        .fold(new BulletAppend(), listOfBullets
            .filter2(new BulletHit3(), this.listOfShips)
            .map(new Split())
            .fold(new BulletExpand(), new MtList<Bullet>()))
        .filter(new BulletOutside()),
    this.bulletNumber,
    this.timeCalculator + 1,
    this.destroyedShip + (this.listOfShips.fold(
        new Length2(), 0) - this.listOfShips.filter2(
            new ShipHit2(), this.listOfBullets).fold(new Length2(), 0)));
    }
  }
  
  //change the world according to the key you press
  public World onKeyEvent(String key) {
    if (key.equals(" ")) {
      if (this.bulletNumber > 0) {
        return new GameWorld(
            this.listOfShips.filter2(new ShipHit2(), this.listOfBullets),
            new ConsList<Bullet>(
            new Bullet(250, 300, 2, Color.PINK, 0, -8, 0),
            this.listOfBullets),
            this.bulletNumber - 1,
            this.timeCalculator,
            this.destroyedShip);
      } else {
        return this;
      }
    } else {
      return this;
    }
  }
  
  //end the world under the suitable situation
  public WorldEnd worldEnds() {
    if (this.bulletNumber == 0
        && this.listOfBullets.fold(new Length1(), 0) == 0) {
      return new WorldEnd(true, this.makeScene());
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }
}
  




class Example {
  Ship s1 = new Ship(1, 1, 1, Color.BLACK, 1, 1);
  Ship s2 = new Ship(1, 2, 1, Color.BLACK, 1, 1);
  Ship s3 = new Ship(1, 10000, 1, Color.BLACK, 1, 1);
  Ship s4 = new Ship(10000, 4, 1, Color.BLACK, 1, 1);
  Bullet b1 = new Bullet(1, 1, 2, Color.BLACK, 1, 1, 0);
  Bullet b2 = new Bullet(1, 2, 2, Color.BLACK, 1, 1, 0);
  Bullet b3 = new Bullet(1, 3000, 2, Color.BLACK, 1, 1, 0);
  Bullet b4 = new Bullet(1000, 4, 2, Color.BLACK, 1, 1, 4);
  
  IList<Ship> ShipEmpty = new MtList<Ship>();
  IList<Ship> S1 = new ConsList<Ship>(s1,
      new ConsList<Ship>(s2, new MtList<Ship>()));
  IList<Ship> S2 = new ConsList<Ship>(s3,
      new ConsList<Ship>(s4, new MtList<Ship>()));
  IList<Bullet> BulletEmpty = new MtList<Bullet>();
  IList<Bullet> B1 = new ConsList<Bullet>(b1, 
      new ConsList<Bullet>(b2, new MtList<Bullet>()));
  IList<Bullet> B2 = new ConsList<Bullet>(b3, 
      new ConsList<Bullet>(b4, new MtList<Bullet>()));
  
 
  //test drawCircle method
  boolean testDrawCircle(Tester t) {
    return t.checkExpect(s1.drawCircle(), 
        new CircleImage(1, OutlineMode.SOLID, Color.BLACK))
        && t.checkExpect(b1.drawCircle(),
            new CircleImage(2, OutlineMode.SOLID, Color.BLACK));
  }
  
  //test radians method
  boolean testRadians(Tester t) {
    return t.checkInexact(b1.radians(180), Math.PI, 0.01)
        && t.checkInexact(s1.radians(180), Math.PI, 0.01);
  }
  
  //test rotate method
  boolean testRotate(Tester t) {
    return t.checkExpect(b1.rotate(1), new Bullet(1, 1, 4, Color.black,
        -8, 0, 1))
        && t.checkExpect(b4.rotate(1), new Bullet(1000, 4, 10, Color.black,
            4, -6, 5))
        && t.checkExpect(s1.rotate(1), null);
  }
  
  //test rotateList method
  boolean testRotateList(Tester t) {
    return t.checkExpect(s1.rotateList(1), null)
        && t.checkExpect(b1.rotateList(1), new ConsList<Bullet>(
            new Bullet(1, 1, 4, Color.black,
                -8, 0, 1), new ConsList<Bullet>(
                    new Bullet(1, 1, 4, Color.black,
                        8, 0, 1), new MtList<Bullet>())))
        && t.checkExpect(b1.rotateList(0), new ConsList<Bullet>(
            new Bullet(1, 1, 4, Color.black,
                8, 0, 1), new MtList<Bullet>()));
  }
  
  //test the randomShip Method
  boolean testRandomShip(Tester t) {
    return t.checkExpect(b1.randomShip(), null);
  }
  
  //test the BiFunction Length1
  boolean testLength1(Tester t) {
    return t.checkExpect(B1.fold(new Length1(), 0), 2)
        && t.checkExpect(BulletEmpty.fold(new Length1(), 0), 0);        
  }
  
  //test the BiFunction Length12
  boolean testLength2(Tester t) {
    return t.checkExpect(S1.fold(new Length2(), 0), 2)
        && t.checkExpect(ShipEmpty.fold(new Length2(), 0), 0);        
  }
  
  //test the BiFunction DrawBullet
  boolean testDrawBullet(Tester t) {
    WorldScene scene = new WorldScene(500, 300);
    
    return t.checkExpect(B1.fold(new DrawBullet(), scene), 
        scene.placeImageXY(b2.drawCircle(), b2.x, b2.y)
        .placeImageXY(b1.drawCircle(), b1.x, b1.y))
        && t.checkExpect(BulletEmpty.fold(new DrawBullet(),  scene), scene);
  }
  
  //test the BiFunction DrawShip
  boolean testDrawShip(Tester t) {
    WorldScene scene = new WorldScene(500, 300);
    
    return t.checkExpect(S1.fold(new DrawShip(), scene), 
        scene.placeImageXY(s2.drawCircle(), s2.x, s2.y)
        .placeImageXY(s1.drawCircle(), s1.x, s1.y))
        && t.checkExpect(ShipEmpty.fold(new DrawShip(),  scene), scene);
  }
  
  //test the Predicate ShipOutside
  boolean testShipOutside(Tester t) {
    return t.checkExpect(S1.filter(new ShipOutside()), S1)
        && t.checkExpect(S2.filter(new ShipOutside()), new MtList<Ship>())
        && t.checkExpect(ShipEmpty.filter(new ShipOutside()), new MtList<Ship>());
  }
  
  //test the Predicate ShipOutside
  boolean testBulletOutside(Tester t) {
    return t.checkExpect(B1.filter(new BulletOutside()), B1)
        && t.checkExpect(B2.filter(new BulletOutside()), new MtList<Bullet>())
        && t.checkExpect(BulletEmpty.filter(new BulletOutside()), new MtList<Bullet>());
  }
  
  //test the Function Split
  boolean testSplit(Tester t) {
    return t.checkExpect(B1.map(new Split()), new ConsList<IList<Bullet>>(
        new ConsList<Bullet>(new Bullet(1, 1, 4, Color.black, -8, 0, 1),
            new ConsList<Bullet>(new Bullet(1, 1, 4, Color.black, 8, 0, 1), 
                new MtList<Bullet>())),
        new ConsList<IList<Bullet>>(
        new ConsList<Bullet>(new Bullet(1, 2, 4, Color.black, -8, 0, 1),
            new ConsList<Bullet>(new Bullet(1, 2, 4, Color.black, 8, 0, 1), 
                new MtList<Bullet>())), new MtList<IList<Bullet>>())))
        && t.checkExpect(BulletEmpty.map(new Split()), new MtList<IList<Bullet>>());
  }
  
  //test the Predicate2 BulletHit
  boolean testBulletHit(Tester t) {
    return t.checkExpect(B1.filter2(new BulletHit(), s1), B1)
        && t.checkExpect(B2.filter2(new BulletHit(), s1), new MtList<Bullet>())
        && t.checkExpect(BulletEmpty.filter2(new BulletHit(), s1), new MtList<Bullet>());
  }
  
  //test the Predicate2 BulletHit
  boolean testShipHit(Tester t) {
    return t.checkExpect(S1.filter2(new ShipHit(), b1), S1)
        && t.checkExpect(S2.filter2(new ShipHit(), b4), new MtList<Ship>())
        && t.checkExpect(ShipEmpty.filter2(new ShipHit(), b1), new MtList<Ship>());
  }
  
  //test the Predicate2 BulletHit2
  boolean testBulletHit2(Tester t) {
    return t.checkExpect(B1.filter2(new BulletHit2(), S1), new MtList<Bullet>())
        && t.checkExpect(B2.filter2(new BulletHit2(), S1), B2)
        && t.checkExpect(BulletEmpty.filter2(new BulletHit2(), S1), new MtList<Bullet>());
  }
  
  //test the Predicate2 BulletHit3
  boolean testBulletHit3(Tester t) {
    return t.checkExpect(B1.filter2(new BulletHit3(), S1), B1)
        && t.checkExpect(B2.filter2(new BulletHit3(), S1), new MtList<Bullet>())
        && t.checkExpect(BulletEmpty.filter2(new BulletHit3(), S1), new MtList<Bullet>());
  }
  
  //test the Predicate2 BulletHit2
  boolean testShipHit2(Tester t) {
    return t.checkExpect(S1.filter2(new ShipHit2(), B2), S1)
        && t.checkExpect(S1.filter2(new ShipHit2(), B1), new MtList<Ship>())
        && t.checkExpect(ShipEmpty.filter2(new ShipHit2(), B1), new MtList<Ship>());
  }
  
  //test the Function BulletMove
  boolean testBulletMove(Tester t) {
    return t.checkExpect(B1.map(new BulletMove()), new ConsList<Bullet>(
        new Bullet(b1.x + b1.speedX, b1.y + b1.speedY, b1.radius, b1.color,
            b1.speedX, b1.speedY, b1.boom), new ConsList<Bullet>(
                new Bullet(b2.x + b2.speedX, b2.y + b2.speedY, b2.radius, b2.color,
                    b2.speedX, b2.speedY, b2.boom), new MtList<Bullet>())))
        && t.checkExpect(BulletEmpty.map(new BulletMove()), new MtList<Bullet>());
  }
  
  //test the Function ShipMove
  boolean testShipMove(Tester t) {
    return t.checkExpect(S1.map(new ShipMove()), new ConsList<Ship>(
        new Ship(s1.x + s1.speedX, s1.y, s1.radius, s1.color,
            s1.speedX, s1.speedY), new ConsList<Ship>(
                new Ship(s2.x + s2.speedX, s2.y, s2.radius, s2.color,
                    s2.speedX, s2.speedY), new MtList<Ship>())))
        && t.checkExpect(ShipEmpty.map(new ShipMove()), new MtList<Ship>());
  }
  
  
  //test the BiFunction BulletAppend
  boolean testBulletAppend(Tester t) {
    return t.checkExpect(B1.fold(new BulletAppend(), B2), 
        new ConsList<Bullet>(b1, new ConsList<Bullet>(b2, B2)))
        && t.checkExpect(BulletEmpty.fold(new BulletAppend(), B2), B2);
  }
  
  //test the BiFunction BulletExpand
  boolean testBulletExpand(Tester t) {
    return t.checkExpect(new ConsList<IList<Bullet>>(B1, 
        new ConsList<IList<Bullet>>(B2, new MtList<IList<Bullet>>()))
        .fold(new BulletExpand(),
            new MtList<Bullet>()), B1.fold(new BulletAppend(), B2))
        && t.checkExpect(new MtList<IList<Bullet>>()
            .fold(new BulletExpand(),
                new MtList<Bullet>()), new MtList<Bullet>());
  }
 
  //test the whole world
  boolean testGo(Tester t) {
    GameWorld myWorld = new GameWorld();
    
    return myWorld.bigBang(500, 300, 0.0357);
  }
}





