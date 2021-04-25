#!/usr/bin/python
# -*- coding: UTF-8 -*-

import xml.etree.ElementTree as ET
import xmltodict
import sys
import os
import re
import time
import argparse


def load_xml(path):
    tree = ET.parse(path)
    return tree


def write_xml(tree, out_path):
    tree.write(out_path, encoding="utf-8", xml_declaration=True)


def load_float(obj, name, default=None):
    if not obj.get(name): return default
    return float(obj.get(name))


def convert(in_path, out_folder, out_name, x, y):
    print('[INFO] Converting...: %s' % (in_path))
    tree = load_xml(in_path)
    xml_entities = tree.getroot().findall('entity')

    for e in xml_entities:
        xml_anis = e.findall('animation')
        for a in xml_anis:
            xml_timelines = a.findall('timeline')
            for xml_timeline in xml_timelines:
                xml_kfrms = xml_timeline.findall('key')
                for xml_kfrm in xml_kfrms:
                    objs = xml_kfrm.findall('object')
                    for obj in objs:
                        _x = load_float(obj, 'x')
                        _y = load_float(obj, 'y')
                        if _x:
                            obj.set('x', str(round((_x + x), 6)))
                        if _y:
                            obj.set('y', str(round((_y + y), 6)))

    name = in_path
    names = []
    if str.find(r'/', in_path) >= 0:
        names = str.split(in_path, '/')
    else:
        names = str.split(in_path, '\\')
    if len(names) > 0:
        name = names[len(names) - 1]
    out_path = os.path.join(out_folder, 'c_' + name)
    write_xml(tree, out_path)


def makedir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc:
        if exc.errno != errno.EEXIST:
            raise


def main(in_path, out_path, x, y):
    if not os.path.exists(in_path):
        print('[ERROR] \'input path\': %s unexist!' % (in_path))
        return

    print('[INFO] Start convert: %s => %s' % (in_path, out_path))

    if not os.path.isdir(in_path):
        file_name = None
        if os.path.isdir(out_path) or out_path.endswith('/') or out_path.endswith('\\'):
            if not os.path.exists(out_path):
                makedir_p(out_path)

            file_name = os.path.splitext(os.path.basename(in_path))[0]
        else:
            file_name = os.path.splitext(os.path.basename(out_path))[0]
            out_path = os.path.dirname(out_path)

        convert(in_path, out_path, file_name, x, y)

    for root, dirs, files in os.walk(in_path):
        out_folder = os.path.join(out_path, re.sub(r'^[\\/]', '', root.replace(in_path, '')))

        if not os.path.exists(out_folder):
            makedir_p(out_folder)

        for f in files:
            if not f.lower().endswith('.scml'): continue
            in_file = os.path.join(root, f)
            out_file = os.path.splitext(f)[0]
            convert(in_file, out_folder, out_file, x, y)

    print('[INFO] End convert!')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='A simple converter that use for spriter file convert to spine file.')
    parser.add_argument('-i', type=str, dest='in_path',
                        help='Directory or File to be converted. By default, point to the current directory.')
    parser.add_argument('-o', type=str, dest='out_path',
                        help='Directory or File convert to. By default, point to the current directory.')
    parser.add_argument('-x', type=float, dest='x_offset', help='change offset x-axis.')
    parser.add_argument('-y', type=float, dest='y_offset', help='change offset y-axis.')
    args = parser.parse_args()

    main(args.in_path or './', args.out_path or './', args.x_offset or 0.0, args.y_offset or 0.0)
