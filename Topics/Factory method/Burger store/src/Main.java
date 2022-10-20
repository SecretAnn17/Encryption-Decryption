class TestDrive {
    public static void main(String[] args) throws InterruptedException {
        BurgerFactory burgerFactory = new BurgerStore();
        Burger chinese = burgerFactory.createBurger("Chinese Burger");
        Burger american = burgerFactory.createBurger("American Burger");
        Burger russian = burgerFactory.createBurger("Russian Burger");
        burgerFactory.orderBurger(chinese.getName());
        burgerFactory.orderBurger(american.getName());
        burgerFactory.orderBurger(russian.getName());

    }
}

abstract class BurgerFactory {

    abstract Burger createBurger(String type);

    Burger orderBurger(String type) throws InterruptedException {
        Burger burger = createBurger(type);
        if (burger == null) {
            System.out.println("Sorry, we are not able to create this kind of burger\n");
            return null;
        }
        System.out.println("Making a " + burger.getName());
        burger.putBun();
        burger.putCutlet();
        burger.putSauce();
        Thread.sleep(1500L);
        System.out.println("Done a " + burger.getName() + "\n");
        return burger;
    }
}

class BurgerStore extends BurgerFactory {
    @Override
    Burger createBurger(String type) {
        switch (type) {
            case "Chinese Burger": return new ChineseBurger(type);
            case "American Burger": return new AmericanBurger(type);
            case "Russian Burger": return new RussianBurger(type);
            default:
                return null;
        }
    }
}

abstract class Burger {
    private String name;

    Burger(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void putBun() {
        System.out.println("Putting bun");
    }

    void putCutlet() {
        System.out.println("Putting cutlet");
    }

    void putSauce() {
        System.out.println("Putting sauce");
    }

}

class ChineseBurger extends Burger {
    public ChineseBurger(String name) {
        super(name);
    }
}

class AmericanBurger extends Burger {
    public AmericanBurger(String name) {
        super(name);
    }
}

class RussianBurger extends Burger {
    public RussianBurger(String name) {
        super(name);
    }
}