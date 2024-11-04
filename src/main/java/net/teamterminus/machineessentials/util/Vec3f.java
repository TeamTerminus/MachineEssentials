package net.teamterminus.machineessentials.util;




import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec2f;

public class Vec3f {
    public double x;
    public double y;
    public double z;

    public Vec3f(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

	public Vec3f(){
        this.x = this.y = this.z = 0;
    }

    public Vec3f(double size){
        this.x = this.y = this.z = size;
    }

    public Vec3f(NbtCompound tag){
        readFromNBT(tag);
    }

    public double distanceTo(Vec3f vec3f) {
        double d = vec3f.x - this.x;
        double d1 = vec3f.y - this.y;
        double d2 = vec3f.z - this.z;
        return MathHelper.sqrt(d * d + d1 * d1 + d2 * d2);
    }

    public Vec3f add(double value){
        this.x += value;
        this.y += value;
        this.z += value;
        return this;
    }

    public Vec3f subtract(double value){
        this.x -= value;
        this.y -= value;
        this.z -= value;
        return this;
    }

    public Vec3f divide(double value){
        this.x /= value;
        this.y /= value;
        this.z /= value;
        return this;
    }

    public Vec3f multiply(double value){
        this.x *= value;
        this.y *= value;
        this.z *= value;
        return this;
    }

    public Vec3f add(Vec3f value){
        this.x += value.x;
        this.y += value.y;
        this.z += value.z;
        return this;
    }

    public Vec3f subtract(Vec3f value){
        this.x -= value.x;
        this.y -= value.y;
        this.z -= value.z;
        return this;
    }

    public Vec3f divide(Vec3f value){
        this.x /= value.x;
        this.y /= value.y;
        this.z /= value.z;
        return this;
    }

    public Vec3f multiply(Vec3f value){
        this.x *= value.x;
        this.y *= value.y;
        this.z *= value.z;
        return this;
    }

    public void writeToNBT(NbtCompound tag){
        tag.putDouble("x",this.x);
        tag.putDouble("y",this.y);
        tag.putDouble("z",this.z);
    }

    public void readFromNBT(NbtCompound tag){
        this.x = tag.getDouble("x");
        this.y = tag.getDouble("y");
        this.z = tag.getDouble("z");
    }

    public Vec3f copy(){
        return new Vec3f(this.x,this.y,this.z);
    }

	public Vec3f floor(){
		this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        this.z = Math.floor(this.z);
		return this;
	}

	public Vec3f ceil(){
		this.x = Math.ceil(this.x);
		this.y = Math.ceil(this.y);
		this.z = Math.ceil(this.z);
		return this;
	}

	public Vec3f round(){
		this.x = Math.round(this.x);
		this.y = Math.round(this.y);
		this.z = Math.round(this.z);
		return this;
	}

	public Vec3f abs(){
		this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
		return this;
	}

	public Vec3f lerp(Vec3f to, double amount){
		double lerpX = x + (to.x - x) * amount;
		double lerpY = y + (to.y - x) * amount;
		double lerpZ = z + (to.z - x) * amount;
		return new Vec3f(lerpX, lerpY, lerpZ);
	}


    @Override
    public String toString() {
        return "Vec3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec3f vec3I = (Vec3f) o;

        if (x != vec3I.x) return false;
        if (y != vec3I.y) return false;
        return z == vec3I.z;
    }

	public Vec3f set(Direction.Axis axis, double value){
        return switch (axis) {
            case X -> {
                this.x = value;
                yield this;
            }
            case Y -> {
                this.y = value;
                yield this;
            }
            case Z -> {
                this.z = value;
                yield this;
            }
        };
	}

	public double get(Direction.Axis axis){
        return switch (axis) {
            case X -> x;
            case Y -> y;
            case Z -> z;
            default -> 0;
        };
	}


	//creates a vec2f from any 2 non-null values of this vec3f
    public Vec2f toVec2f() {
        double[] nonZeroValues = new double[3];
        int nonZeroCount = 0;

        if (this.x != 0) nonZeroValues[nonZeroCount++] = this.x;
        if (this.y != 0) nonZeroValues[nonZeroCount++] = this.y;
        if (this.z != 0) nonZeroValues[nonZeroCount++] = this.z;

        if (nonZeroCount != 2) return null;
        return new Vec2f((float) nonZeroValues[0], (float) nonZeroValues[1]);
    }

    @Override
    public int hashCode() {
        int result = (int) x;
        result = (int) (31 * result + y);
        result = (int) (31 * result + z);
        return result;
    }
}
