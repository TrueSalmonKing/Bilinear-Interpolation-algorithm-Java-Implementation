							////// SalmonKing ///////

The algorithm used is the program, utilizes the bilinear interpolation, in order to either upscale, or downscale
an image. Let's use the following 4 pixels below (we're taking the example of an upscale by 2, but this can be generalized for both upscaling and downscaling):

				           <--------1------>
				           <--w-->
	xxxxxxxxx		     xxxxxxxxxxxxxxxxx ^ ^
	x A x B x		     x A x a x ? x B x | |
	xxxxxxxxx	--->	 xxxxxxxxxxxxxxxxx h |
	x C x D	x		     x ? x ? x ? x ? x | |
	xxxxxxxxx		     xxxxxxxxxxxxxxxxx | 1
				           x ? x P x ? x ? x v |
				           xxxxxxxxxxxxxxxxx   |
				           x C x b x ? x D x   |
				           xxxxxxxxxxxxxxxxx   v

Before we procceed, the notion of bilinear interpolation must be elaborated on, by bilinear we are simply refering to the application of linear interpolation
with an extension onto functions with 2 variables. In this interpolation we apply linear interpolation on the x axis, then the y axis and we interpolate finally
between the newly couple of values. We can view this by continuing our example.

In order to fill in the empty pixel values, let's consider 3 vertically aligned points a,b and P.

a, b and P will be the result of performing a linear interpolation 
between A and B, C and D and a and B respectively, such that:
	
	a = A + w(B-A)
	b = C + h(D-C)
	P = a + h(b-a)	---->	P = A + w(B-A) + h[(C + h(D-C)) - (A + w(B-A))]	--->	P = A(1-h)(1-w) + B(w)(1-h) + C(h)(1-w) + D(h)(w)

We finally have the equation that'll allow use to fill in any random pixel chosen in the newly scaled image.
In order to translate this to code we simply have to find out the terms w and h, which are in this case, vertical and horizental weights, since they are weights
they should be float numbers, based on the scale of resizing. The scales of resizing (that are also float numbers) can be computed as follows :

	Xratio = (((float)width-1)/width2)
	Yratio = (((float)height-1)/height2)

with (width,height) being the dimensions of the original image and (width2,height2) being the dimensions of the scaled image.

	x = (int)(Xratio * i)
	y = (int)(Yratio * j)
	w = (Xratio * i) - x
	h = (Yratio * j) - y

with (i,j) being the coordinates of the pixel to be computed in the scaled image, and (x,y) being the coordinates of the closest top left pixel in the original image.
thus we can conclude the closest surrouding 4 pixels in the original image, which are A,B,C and D.
Finally we apply the bilinear interpolation to each channel of the pixel, if it's ARGB we apply it 4 times to each channel as follows:
	
	red = ((A & 0xff)*(1-y2)*(1-x2) + (B & 0xff)*x2*(1-y2) + (C & 0xff)*y2*(1-x2) + (D & 0xff)*y2*x2);
	green = ((A>>8 & 0xff)*(1-y2)*(1-x2) + (B>>8 & 0xff)*x2*(1-y2) + (C>>8 & 0xff)*y2*(1-x2) + (D>>8 & 0xff)*y2*x2);
	blue = ((A>>16 & 0xff)*(1-y2)*(1-x2) + (B>>16 & 0xff)*x2*(1-y2) + (C>>16 & 0xff)*y2*(1-x2) + (D>>16 & 0xff)*y2*x2);
	alpha = ((A>>24 & 0xff)*(1-y2)*(1-x2) + (B>>24 & 0xff)*x2*(1-y2) + (C>>24 & 0xff)*y2*(1-x2) + (D>>24 & 0xff)*y2*x2);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

This algorithm is unfit for upscaling or downscaling more than twice, as such if it's necessary, it is recommended to scale in succession. For example
if we want to scale it from 400x400 to 1600x1600, it is better to scale it from 400x400 to 800x800 to 1600x1600. 
	
