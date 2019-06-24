package solarsystem.rocket;

import solarsystem.Trajectory;


//Not the most realistic Falcon Heavy spacecraft; but the values should get us to titan and back
//with some extra fuel we are carrying (we really have to carry alot of fuel)
public abstract class Falcon9Imaginary extends SpaceCraft {
    public Falcon9Imaginary() {
        /*
            Center core + 2 boosters dry weight : 22200kg
            Second Stage weight                 :  4000kg
            Payload fairing weight              :  1700kg
         */
        dryMass = 22200 + 4000 + 1700;

        /*
            A typical mission fuel amount:

            Center core + 2 boosters LOX weight : 287400kg
            Center core + 2 boosters Kerosene w.: 123500kg
            Second Stage LOX weight             :  75200kg
            Second Stage Kerosene weight        :  32300kg
         */
        fuelMass = 287400 + 123500 + 75200 + 32300;
        this.mass = fuelMass + dryMass;

        /*
            Sea level   : 282
            Vacuum      : 311
            Most of the time rocket will spend it's life at space,
            assumed more inclined towards vacumm impulse
         */
        //TODO: check this value, i multiplied it by 1000 as it is often in kn
        thrusterImpulse = 305 * 1000;

        /*
            Literally no data about this, some guy calculated it:
            https://www.reddit.com/r/spacex/comments/1xhuok/legbased_stability_and_moments_of_inertia/

            assuming it's true... hopefully.
         */
        J = 98080;

        //First stage maximum thrust in vaccumm
        maxFtPropulsion = 24681 * 1000; //kN to N
    }
}
