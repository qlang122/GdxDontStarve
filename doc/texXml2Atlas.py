#!/usr/bin/python
# -*- coding: UTF-8 -*-

import xml.etree.ElementTree as ET
import xmltodict
import sys
import os
import argparse
import re

def load_xml(path):
    tree = ET.parse(path)
    xml_data = tree.getroot()
    xmlstr = ET.tostring(xml_data, encoding='utf8', method='xml')
    data_dict = dict(xmltodict.parse(xmlstr))
    return data_dict


def write_text(data, path): 
    with open(path, 'w+') as f:
        f.write(data)

def load_tags(obj, name):
    if name not in obj:
        return [] 

    tag_objs = obj[name]
    if type(tag_objs).__name__ != 'list':
        return [tag_objs]

    return tag_objs

def extract_xml_data(in_path):
    xml_root = load_xml(in_path)['Atlas']

    xml_tex = 'Texture' in xml_root and xml_root['Texture']['@filename'] or ''
    xml_elements = xml_root['Elements']
    
    elements = []
    xml_eles = load_tags(xml_elements, 'Element')
    for _, e in enumerate(xml_eles):
        eobj = {
                'name': e['@name'],
                'u1': e['@u1'],
                'u2': e['@u2'],
                'v1': e['@v1'],
                'v2': e['@v2']
        }
        # print(eobj)
        elements.append(eobj)
    
    return (xml_tex, elements)

def check_is_nums(elements):
    for n in elements:
        if not re.match(r'\d', str(n['name'])):
            return False
    return True
    
def makeElementTxt(name, x, y, w, h, index):
    return '''%s\n  rotate: false\n  xy: %s, %s\n  size: %s, %s\n  orig: %s, %s\n  offset: 0, 0\n  index: %s\n''' % (name, int(x), int(y), int(w), int(h), int(w), int(h), index)

# 算法规则
# a,b为图像宽高
# u1 = x / a
# x = a * u1
#
# u2 = (x + w)/a
# w = a * u2 - x
#
# v1 = (b-y-h)/b
# b-y-h = b*v1
# h = b-y-b*v1
#
# v2 = (b-y)/b
# y = b-b*v2
def convert2atlas(elements, tex_name, img_w, img_h):
    names = str.split(tex_name, '/')
    count = len(names)
    _name = ''
    if count > 0:
        name = names[count-1]
        ns = str.split(name, '.')
        if len(ns) > 0:
            _name = ns[0]

    txt_head = '\n%s.png\nformat: RGBA8888\nfilter: Nearest,Nearest\nrepeat: none\n' % (_name)

    isIndex = check_is_nums(elements)
    
    entitys =[]
    for e in elements:
        x = float(e['u1']) * img_w
        y = img_h - float(e['v2']) * img_h
        w = float(e['u2']) * img_w - x
        h = img_h - float(e['v1']) * img_h - y
        if not isIndex:
            name = e['name']
            ns = str.split(name, '.')
            if len(ns) > 0:
                name = ns[0]
            entitys.append(makeElementTxt(name, x, y, w, h, '-1'))
        else:
            entitys.append(makeElementTxt(_name, x, y, w, h, int(e['name'])))
    return (_name, txt_head, entitys)

def convert(in_path, out_folder, out_name, img_w, img_h):
    print ('[INFO] Converting...: %s' % (in_path))
    tex_name, elements = extract_xml_data(in_path)
    name, txt_head, entitys = convert2atlas(elements, tex_name, img_w, img_h)

    output_path = os.path.join(out_folder, name + '.atlas')
    with open(output_path, 'w+') as f:
        f.write(txt_head)
        for e in entitys:
            f.write(e)

def makedir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc:
        if exc.errno != errno.EEXIST:
            raise

def main(in_path, out_path, img_w, img_h):
    if not os.path.exists(in_path):
        print ('[ERROR] \'input path\': %s unexist!' % (in_path))
        return

    print ('[INFO] Start convert: %s => %s' % (in_path, out_path))

    if not os.path.isdir(in_path):
        file_name = None
        if os.path.isdir(out_path) or out_path.endswith('/') or out_path.endswith('\\'):
            if not os.path.exists(out_path):
                makedir_p(out_path)

            file_name = os.path.splitext(os.path.basename(in_path))[0]
        else:
            file_name = os.path.splitext(os.path.basename(out_path))[0]
            out_path = os.path.dirname(out_path)

        convert(in_path, out_path, file_name, img_w, img_h)


    for root, dirs, files in os.walk(in_path):
        out_folder = os.path.join(out_path, re.sub(r'^[\\/]', '', root.replace(in_path, '')))

        if not os.path.exists(out_folder):
            makedir_p(out_folder)

        for f in files:
            if not f.lower().endswith('.xml'): continue
            in_file = os.path.join(root, f)
            out_file = os.path.splitext(f)[0]
            convert(in_file, out_folder, out_file, img_w, img_h)

    print ('[INFO] End convert!')

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='A simple converter that use for spriter file convert to spine file.')
    parser.add_argument('-i', type=str, dest='in_path', help='Directory or File to be converted. By default, point to the current directory.')
    parser.add_argument('-o', type=str, dest='out_path', help='Directory or File convert to. By default, point to the current directory.')
    parser.add_argument('-w', type=float, dest='img_width', help='The base image width.')
    parser.add_argument('-hh', type=float, dest='img_height', help='The base image height.')
    args = parser.parse_args()

    main(args.in_path or './', args.out_path or './', args.img_width or 0.0, args.img_height or 0.0)