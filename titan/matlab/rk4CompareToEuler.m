close all; clear; clc;
G = 6.674e-11;
mC = 1.989e30; % Mass of central body

origin = zeros(3,1);

% initial position and velocity in m
x0 = [-1.471633868509571E+11; 2.104852097662997E+10; -2.126817645682022E+05];
v0 = [-4.692621973980529E+03; -2.960544243231639E+04; 6.108724178606195E-01];

tf = 365 * 24 * 60 * 60;  % final t in seconds
n = 365 * 24; % step
h = tf/n; % step size

ts = linspace(0, tf, n+1);
ys = zeros(6, n+1);
ys(:, 1) = [x0;v0];

ys2 = zeros(6, n+1);
ys2(:, 1) = [x0;v0];

for i = 1:n
     ys(:, i+1) = ys(:, i) + deriv(ys(:, i), G, mC, origin) * h;
     ys2(:, i+1) = ys2(:, i) + rk4(ys2(:, i), G, mC, origin, h);
end

figure;

plot3(ys(1, :), ys(2, :), ys(3, :), 'r');
title("euler");
figure;
plot3(ys2(1, :), ys2(2, :), ys2(3, :), 'b');
title("rk4");

 
% doty [ds, dv]
% this is equal to euler method
function [doty] = deriv(y, G, mC, origin)
    doty = zeros(6, 1);
    doty(1:3) = y(4:6);
    s = origin - y(1:3);
    r3 = norm(s)^3;
    doty(4:6) = s * G * mC / r3;
end


function [doty] = rk4(y,G,mC,origin, h)
    k1 = h * deriv(y, G, mC, origin);
    yp = y + 1/2*k1;
    k2 = h * deriv(yp, G, mC, origin);
    yp = y + 1/2*k2;
    k3 = h * deriv(yp, G, mC, origin);
    yp = y + k3;
    k4 = h * deriv(yp,G,mC,origin);
    doty =1/6*(k1+2*(k2+k3)+k4);
end
    













