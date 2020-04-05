package com.BilinearInterpolation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageResize {
	
	public static int[] Resize(int[] pixels, int w, int h, int w2, int h2) {
		int[] pixelsOut = new int[w2*h2];
		float Xratio = (((float)w-1)/w2);
		float Yratio = (((float)h-1)/h2);
		float x2,y2;
		int A,B,C,D,x,y,index;
		float blue,green,red,alpha;
		for(int j=0;j<h2;j++) {
			for(int i=0;i<w2;i++) {
				x = (int)(Xratio * i);
				y = (int)(Yratio * j);
				x2 = (Xratio * i) - x;
				y2 = (Yratio * j) - y;
				index = (y*w + x);
				A = pixels[index];
				B = pixels[index+1];
				C = pixels[index+w];
				D = pixels[index+w+1];
				
				red = ((A & 0xff)*(1-y2)*(1-x2) + (B & 0xff)*x2*(1-y2) + (C & 0xff)*y2*(1-x2) + (D & 0xff)*y2*x2);
				green = ((A>>8 & 0xff)*(1-y2)*(1-x2) + (B>>8 & 0xff)*x2*(1-y2) + (C>>8 & 0xff)*y2*(1-x2) + (D>>8 & 0xff)*y2*x2);
				blue = ((A>>16 & 0xff)*(1-y2)*(1-x2) + (B>>16 & 0xff)*x2*(1-y2) + (C>>16 & 0xff)*y2*(1-x2) + (D>>16 & 0xff)*y2*x2);
				alpha = ((A>>24 & 0xff)*(1-y2)*(1-x2) + (B>>24 & 0xff)*x2*(1-y2) + (C>>24 & 0xff)*y2*(1-x2) + (D>>24 & 0xff)*y2*x2);
				
				pixelsOut[i+j*w2] = ((int)red) | (((int)green)<<8) | (((int)blue)<<16) | (((int)alpha)<<24);
			}
		}
		
		return pixelsOut;
	}
	
	public static void main(String[] args) {
		int w = 1, h = 1, w2 = 50, h2 = 50;
		int[] pixels = null;
		BufferedImage image = null;
		BufferedImage imageOut = null;
		
		try {
			File file = new File("OriginalImage.png");
			image = new BufferedImage(w,h, BufferedImage.TYPE_4BYTE_ABGR);
			image = ImageIO.read(file);
			w = image.getWidth();
			h = image.getHeight();
			w2 = w*2;
			h2 = h*2;
			pixels = new int[w*h];
			
			for(int j=0;j<h;j++) {
				for(int i=0;i<w;i++) {
					pixels[i + j*w] = image.getRGB(i, j);
				}
			}
		}
		catch(IOException e) {
			System.out.println("File couldn't be opened !");
		}
		
		pixels = Resize(pixels,w,h,w2,h2);
		
		try {
			File fileOut = new File("ScaledImage.png");
			imageOut = new BufferedImage(w2,h2, BufferedImage.TYPE_4BYTE_ABGR);
			
			for(int j=0;j<h2;j++) {
				for(int i=0;i<w2;i++) {
					imageOut.setRGB(i, j, pixels[i + j*w2]);
				}
			}
			
			ImageIO.write(imageOut, "png", fileOut);
		}
		catch(IOException e) {
			System.out.println("File couldn't be opened !");
		}
	}
}
